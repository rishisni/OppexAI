import React, { useState } from "react";
import AuthForm from "../components/authForm";
import { signup } from "../services/auth";
import { Snackbar, Alert, Box, Card, CardContent, Typography } from "@mui/material";

export default function SignupPage() {
  const [message, setMessage] = useState("");
  const [open, setOpen] = useState(false);

  const handleSignup = async (email, password) => {
    try {
      const res = await signup(email, password);
      setMessage(res.data.message || "Signup successful! Please check your email to verify.");
      setOpen(true);
    } catch (err) {
      setMessage(err.response?.data?.message || "Signup failed ");
      setOpen(true);
    }
  };

  return (
     <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Card sx={{ width: 400, p: 3, boxShadow: 0 ,bgcolor :"white"}}>
        <CardContent>
          <AuthForm title="Signup" onSubmit={handleSignup} />
        </CardContent>
      </Card>

      <Snackbar
        open={open}
        autoHideDuration={4000}
        onClose={() => setOpen(false)}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert
          onClose={() => setOpen(false)}
          severity={message.includes("failed") ? "error" : "success"}
          sx={{ width: "100%" }}
        >
          {message}
        </Alert>
      </Snackbar>
    </Box>
  );
}
