import React, { useState } from "react";
import { TextField, Button, Box, Typography, Paper } from "@mui/material";

export default function AuthForm({ title, onSubmit }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(email, password);
  };

  return (
    <Paper
      elevation={6}
      sx={{ p: 4, maxWidth: 400, mx: "auto", mt: 8, borderRadius: 3 }}
    >
      <Typography variant="h5" gutterBottom>
        {title}
      </Typography>
      <Box component="form" onSubmit={handleSubmit} sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
        <TextField
          label="Email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          fullWidth
        />
        <TextField
          label="Password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          fullWidth
        />
        <Button type="submit" variant="contained" fullWidth>
          {title}
        </Button>
      </Box>
    </Paper>
  );
}
