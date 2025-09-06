// src/components/Navbar.js
import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { AppBar, Toolbar, Typography, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

export default function Navbar({ toggleTheme }) {
  const { user, loading, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate("/login");
  };


  return (
    <AppBar position="static" color="primary">
  <Toolbar>
    <Typography
      variant="h6"
      sx={{ flexGrow: 1, textDecoration: "none", color: "inherit" }}
      component={Link}
      to="/"
    >
      Portal
    </Typography>

    {loading ? null : user ? (
      <>
        <Button  variant="contained"
          color="danger" onClick={handleLogout}>
          Logout
        </Button>
      </>
    ) : (
      <>
        <Button
          variant="contained"
          color="secondary"
          component={Link}
          to="/signup"
        >
          Signup
        </Button>
        <Button
          variant="contained"
          color="secondary"
          component={Link}
          to="/login"
          sx={{ ml: 2 }} 
        >
          Login
        </Button>
      </>
    )}
  </Toolbar>
</AppBar>

  );
}
