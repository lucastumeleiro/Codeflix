import { Box, Button, IconButton, Typography } from "@mui/material"
import { useAppDispatch, useAppSelector } from "../../app/hooks"
import { deleteCategory, selectCategories } from "./categoriesSlice"
import { Link, useNavigate } from "react-router-dom"
import type {
  GridColDef,
  GridRenderCellParams,
  GridRowsProp,
} from "@mui/x-data-grid"
import { DataGrid } from "@mui/x-data-grid"
import DeleteIcon from "@mui/icons-material/Delete"
import EditIcon from "@mui/icons-material/Edit"
import { useSnackbar } from "notistack"

function CategoryList() {
  const dispatch = useAppDispatch()
  const categories = useAppSelector(selectCategories)
  const navigate = useNavigate()
  const { enqueueSnackbar } = useSnackbar()

  const rows: GridRowsProp = categories.map(category => ({
    id: category.id,
    name: category.name,
    description: category.description,
    is_active: category.is_active,
    created_at: new Date(
      category.created_at ?? new Date().toISOString(),
    ).toLocaleDateString("pt-BR"),
  }))

  const columns: GridColDef[] = [
    { field: "name", headerName: "Name", flex: 1 },
    {
      field: "is_active",
      headerName: "Active",
      width: 160,
      type: "boolean",
      renderCell: renderIsActiveCell,
    },
    { field: "created_at", headerName: "Created At", width: 160 },
    {
      field: "id",
      headerName: "Actions",
      width: 100,
      renderCell: renderActionsCell,
    },
  ]

  function renderIsActiveCell(row: GridRenderCellParams) {
    return (
      <Typography color={row.value ? "primary" : "secondary"}>
        {row.value ? "Active" : "Inactive"}
      </Typography>
    )
  }

  function renderActionsCell(row: GridRenderCellParams) {
    return (
      <>
        <IconButton
          color="primary"
          onClick={() => {
            navigate(`/categories/update/${String(row.id)}`)
          }}
          aria-label="edit"
        >
          <EditIcon />
        </IconButton>
        <IconButton
          color="secondary"
          onClick={() => {
            dispatch(deleteCategory(String(row.id)))
            enqueueSnackbar("Success deleted category", {
              variant: "success",
            })
          }}
          aria-label="delete"
        >
          <DeleteIcon />
        </IconButton>
      </>
    )
  }

  return (
    <Box maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box display="flex" justifyContent="flex-end">
        <Button
          variant="contained"
          color="secondary"
          component={Link}
          to="/categories/create"
          style={{ marginBottom: "1rem" }}
        >
          New Category
        </Button>
      </Box>
      <DataGrid
        rows={rows}
        columns={columns}
        showToolbar
        slotProps={{
          toolbar: {
            showQuickFilter: true,
            quickFilterProps: {
              debounceMs: 500,
            },
          },
        }}
        disableColumnSelector
        disableColumnFilter
        disableDensitySelector
        disableRowSelectionOnClick
        disableColumnResize
        pageSizeOptions={[5, 10, 25, 50, 100]}
      />
    </Box>
  )
}

export { CategoryList }
