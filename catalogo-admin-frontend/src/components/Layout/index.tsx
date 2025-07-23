import { Box, Container } from "@mui/material"
import type { ReactNode } from "react"

function Layout({ children }: { children: ReactNode }) {
  return (
    <Box>
      <Container
        maxWidth="lg"
        sx={{
          mt: 4,
          mb: 4,
          color: theme => theme.palette.text.primary,
        }}
      >
        {children}
      </Container>
    </Box>
  )
}

export { Layout }
