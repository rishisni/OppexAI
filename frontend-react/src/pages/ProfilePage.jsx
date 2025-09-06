// src/pages/ProfilePage.js
import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import {
  Container,
  Card,
  CardContent,
  Typography,
  Snackbar,
  Alert,
  CircularProgress,
  Box,
} from "@mui/material";

export default function ProfilePage() {
  const { user, loading } = useContext(AuthContext);
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);

  useEffect(() => {
    if (!loading && !user) {
      setOpen(true);
      setTimeout(() => navigate("/login"), 1500);
    }
  }, [loading, user, navigate]);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
        <CircularProgress />
      </Box>
    );
  }

  if (!user) {
    return (
      <Snackbar
        open={open}
        autoHideDuration={1500}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert severity="error" sx={{ width: "100%" }}>
          Unauthorized  Please login first
        </Alert>
      </Snackbar>
    );
  }

  return (
    <Container maxWidth="sm" sx={{ mt: 5 }}>
      <Card sx={{ p: 3, textAlign: "center" }}>
        <CardContent>
          <Typography variant="h5" gutterBottom>
            Welcome to your Profile
          </Typography>
          <Typography variant="body1" sx={{ mb: 2 }}>
            Email: {user.email}
          </Typography>
        </CardContent>
      </Card>
    </Container>
  );
}
