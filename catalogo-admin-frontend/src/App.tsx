import { Box, Typography } from "@mui/material"
import "./App.css"
import { Header } from "./components/Header"
import { Layout } from "./components/Layout"
import { Route } from "react-router-dom"
import { Routes } from "react-router-dom"
import { CategoryList } from "./features/categories/CategoryList"
import { CategoryUpdate } from "./features/categories/CategoryUpdate"
import { CategoryCreate } from "./features/categories/CategoryCreate"

function App() {
  return (
    <Box
      component="main"
      sx={{
        height: "100vh",
        backgroundColor: theme => theme.palette.grey[900],
      }}
    >
      <Header />
      <Layout>
        <Routes>
          <Route path="/" element={<CategoryList />} />

          <Route path="/categories" element={<CategoryList />} />
          <Route path="/categories/create" element={<CategoryCreate />} />
          <Route path="/categories/update/:id" element={<CategoryUpdate />} />

          <Route
            path="*"
            element={
              <Box>
                <Typography variant="h1">404 - Not Found</Typography>
                <Typography variant="h2">Page not found</Typography>
              </Box>
            }
          />
        </Routes>
      </Layout>
    </Box>
  )
}

export { App }
