import { Link } from "react-router-dom"
import {
  FormControl,
  FormControlLabel,
  Switch,
  TextField,
  Button,
  Box,
  Grid,
} from "@mui/material"
import type { TCategory } from "../categoriesSlice"

type TCategoryFormProps = {
  category: TCategory
  isDisabled: boolean
  isLoading: boolean
  handleChange: (e: React.ChangeEvent<HTMLInputElement>) => void
  handleToggle: (e: React.ChangeEvent<HTMLInputElement>) => void
  onSubmit: (e: React.FormEvent<HTMLFormElement>) => void
}

function CategoryForm({
  category,
  isDisabled,
  isLoading,
  handleChange,
  handleToggle,
  onSubmit,
}: TCategoryFormProps) {
  return (
    <Box p={2}>
      <form onSubmit={onSubmit}>
        <Grid container spacing={2}>
          <Grid size={{ xs: 12, md: 12, lg: 12, xl: 12 }}>
            <FormControl fullWidth>
              <TextField
                required
                name="name"
                label="Name"
                value={category.name}
                disabled={isDisabled}
                onChange={handleChange}
              />
            </FormControl>
          </Grid>
          <Grid size={{ xs: 12, md: 12, lg: 12, xl: 12 }}>
            <FormControl fullWidth>
              <TextField
                required
                name="description"
                label="Description"
                value={category.description}
                disabled={isDisabled}
                onChange={handleChange}
              />
            </FormControl>
          </Grid>
          <Grid size={{ xs: 12, md: 12, lg: 12, xl: 12 }}>
            <FormControl fullWidth>
              <FormControlLabel
                label="Active"
                control={
                  <Switch
                    name="is_active"
                    color="secondary"
                    disabled={isDisabled}
                    checked={category.is_active}
                    onChange={handleToggle}
                  />
                }
              />
            </FormControl>
          </Grid>

          <Grid size={{ xs: 12, md: 12, lg: 12, xl: 12 }}>
            <Box display="flex" gap={2}>
              <Button
                variant="contained"
                color="primary"
                component={Link}
                to="/categories"
              >
                Back
              </Button>
              <Button
                type="submit"
                variant="contained"
                color="secondary"
                disabled={isLoading || isDisabled}
              >
                {isLoading ? "Saving..." : "Save"}
              </Button>
            </Box>
          </Grid>
        </Grid>
      </form>
    </Box>
  )
}

export { CategoryForm }
