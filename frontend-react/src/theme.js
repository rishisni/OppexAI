// src/theme.js
import { createTheme } from "@mui/material/styles";

const darkTheme = createTheme({
  palette: {
    mode: "dark",
    primary: { main: "#90caf9" },
    secondary: { main: "#ce93d8" },
    danger : {main: "#d20d34ff"},
    background: { default: "#fff", paper: "#000" },
  },
  components: {
    MuiAppBar: {
      styleOverrides: {
        root: {
        
        },
      },
    },
  },
});

export default darkTheme;
