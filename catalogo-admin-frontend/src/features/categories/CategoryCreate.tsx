import { Box, Typography, Paper } from "@mui/material"
import { createCategory, type TCategory } from "./categoriesSlice"
import { useState } from "react"
import { CategoryForm } from "./components/CategoryForm"
import { useAppDispatch } from "../../app/hooks"
import { useSnackbar } from "notistack"

function CategoryCreate() {
  const dispatch = useAppDispatch()
  const [category, setCategory] = useState<TCategory>({
    id: "",
    name: "",
    description: "",
    is_active: true,
    created_at: "",
  })
  const { enqueueSnackbar } = useSnackbar()

  const [isDisabled, setIsDisabled] = useState<boolean>(false)
  const [isLoading, setIsLoading] = useState<boolean>(false)

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    const { name, value } = e.target
    setCategory({
      ...category,
      [name]: value,
    })
  }

  function handleToggle(e: React.ChangeEvent<HTMLInputElement>) {
    const { name, checked } = e.target
    setCategory({
      ...category,
      [name]: checked,
    })
  }

  function onSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault()
    dispatch(createCategory(category))
    enqueueSnackbar("Success created category", {
      variant: "success",
    })
  }

  return (
    <Box>
      <Paper>
        <Box p={2} mb={2}>
          <Typography variant="h4">Create Category</Typography>
        </Box>

        <CategoryForm
          category={category}
          isDisabled={isDisabled}
          isLoading={isLoading}
          handleChange={handleChange}
          handleToggle={handleToggle}
          onSubmit={onSubmit}
        />
      </Paper>
    </Box>
  )
}

export { CategoryCreate }
