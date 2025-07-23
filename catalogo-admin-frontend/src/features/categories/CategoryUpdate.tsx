import { Box, Typography, Paper } from "@mui/material"
import { useAppDispatch, useAppSelector } from "../../app/hooks"
import { useParams } from "react-router"
import type { TCategory } from "./categoriesSlice"
import { selectCategoryById, updateCategory } from "./categoriesSlice"
import { useState } from "react"
import { CategoryForm } from "./components/CategoryForm"
import { useSnackbar } from "notistack"

function CategoryUpdate() {
  const { id } = useParams()
  const dispatch = useAppDispatch()
  const categoryStore = useAppSelector(state =>
    selectCategoryById(state, id ?? ""),
  )
  const { enqueueSnackbar } = useSnackbar()

  const [category, setCategory] = useState<TCategory>(categoryStore)
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
    dispatch(updateCategory(category))
    enqueueSnackbar("Success updated category", {
      variant: "success",
    })
  }

  return (
    <Box>
      <Paper>
        <Box p={2} mb={2}>
          <Typography variant="h4">Update Category</Typography>
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

export { CategoryUpdate }
