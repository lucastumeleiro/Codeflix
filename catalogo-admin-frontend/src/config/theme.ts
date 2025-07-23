import { createTheme } from "@mui/material"

export const darkTheme = createTheme({
  palette: {
    background: { default: "#262626" },
    mode: "dark",
    primary: { main: "#f3f4f6" },
    secondary: { main: "#dc2626" },
    text: { primary: "#f3f4f6" },
  },
})

export const lightTheme = createTheme({
  palette: {
    background: {},
    mode: "light",
    primary: { main: "#dc2626" },
    secondary: { main: "#262626" },
    text: { primary: "#262626" },
  },
})
