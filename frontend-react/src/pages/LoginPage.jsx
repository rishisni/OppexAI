// src/pages/LoginPage.js
import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import {
  Snackbar,
  Alert,
  Box,
  Card,
  CardContent,
  Typography,
} from "@mui/material";
import { login as loginService } from "../services/auth";
import api from "../api/index";
import { AuthContext } from "../context/AuthContext";
import AuthForm from "../components/authForm"; 

export default function LoginPage() {
  const [message, setMessage] = useState("");
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();
  const { login } = useContext(AuthContext);

  const handleLogin = async (email, password) => {
    try {
      const res = await loginService(email, password);

      const profile = await api.get("/me");

      login(profile.data);

      localStorage.setItem("userEmail", profile.data.email);

      setMessage(res.data.message || "Login successful ");
      setOpen(true);

      setTimeout(() => navigate("/profile"), 500);
    } catch (err) {
      setMessage(err.response?.data?.message || "Login failed ");
      setOpen(true);
    }
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Card sx={{ width: 400, p: 3, boxShadow: 0 ,bgcolor :"white"}}>
        <CardContent>
          

          <AuthForm title="Login" onSubmit={handleLogin} />
        </CardContent>
      </Card>

      <Snackbar
        open={open}
        autoHideDuration={3000}
        onClose={() => setOpen(false)}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert
          onClose={() => setOpen(false)}
          severity={message.includes("failed") ? "error" : "success"}
          variant="filled"
          sx={{ width: "100%" }}
        >
          {message}
        </Alert>
      </Snackbar>
    </Box>
  );
}
