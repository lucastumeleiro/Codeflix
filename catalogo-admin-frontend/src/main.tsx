import { StrictMode } from "react"
import { createRoot } from "react-dom/client"
import { BrowserRouter } from "react-router-dom"
import { Provider } from "react-redux"
import { App } from "./App"
import { store } from "./app/store"
import "./index.css"
import { SnackbarProvider } from "notistack"
import { darkTheme } from "./config/theme"
import { ThemeProvider } from "@mui/material"

const container = document.getElementById("root")

if (container) {
  const root = createRoot(container)

  root.render(
    <StrictMode>
      <BrowserRouter>
        <Provider store={store}>
          <ThemeProvider theme={darkTheme}>
            <SnackbarProvider
              maxSnack={3}
              anchorOrigin={{ vertical: "top", horizontal: "right" }}
              autoHideDuration={3000}
              preventDuplicate
            >
              <App />
            </SnackbarProvider>
          </ThemeProvider>
        </Provider>
      </BrowserRouter>
    </StrictMode>,
  )
} else {
  throw new Error(
    "Root element with ID 'root' was not found in the document. Ensure there is a corresponding HTML element with the ID 'root' in your HTML file.",
  )
}
