import { createSlice, createSelector } from "@reduxjs/toolkit"
import type { RootState } from "../../app/store"
import { apiSlice } from "../api/apiSlice"
import type { TResults } from "../../types/Category"

export type TCategory = {
  id: string
  name: string
  description?: string | null
  is_active: boolean
  created_at?: string
  updated_at?: string | null
  deleted_at?: string | null
}

const endpointUrl = "/categories"
export const categoriesApiSlice = apiSlice.injectEndpoints({
  endpoints: build => ({
    getCategories: build.query<TResults, undefined>({
      query: () => endpointUrl,
      providesTags: ["Categories"],
    }),
  }),
})

export const initialState: TCategory[] = [
  {
    id: "6db60c3e-583a-4dd4-8dcc-2fb009ee6ffc",
    name: "Categoria 1",
    description: "Descrição da categoria 1",
    is_active: true,
    created_at: "2025-06-01T15:47:00+0000",
    updated_at: null,
    deleted_at: null,
  },
  {
    id: "efbcefe4-e853-454a-b0d4-156de15e7757",
    name: "Categoria 2",
    description: "Descrição da categoria 2",
    is_active: true,
    created_at: "2025-06-01T15:49:00+0000",
    updated_at: null,
    deleted_at: null,
  },
  {
    id: "b3cdea67-5247-4973-9c78-3af8a32407e8",
    name: "Lucas",
    description: "Descrição da categoria 3",
    is_active: true,
    created_at: "2025-06-01T15:51:00+0000",
    updated_at: null,
    deleted_at: null,
  },
]

export const categoriesSlice = createSlice({
  name: "categories",
  initialState: initialState,
  reducers: {
    createCategory: (state, action: { payload: TCategory }) => {
      state.push(action.payload)
    },
    updateCategory: (state, action: { payload: TCategory }) => {
      const index = state.findIndex(
        category => category.id === action.payload.id,
      )
      state[index] = action.payload
    },
    deleteCategory: (state, action: { payload: string }) => {
      const index = state.findIndex(category => category.id === action.payload)
      state.splice(index, 1)
    },
  },
})

export const { getCategories } = categoriesApiSlice.endpoints

export const { createCategory, updateCategory, deleteCategory } =
  categoriesSlice.actions

export const selectCategories = (state: RootState) => {
  return state.categories
}

export const selectCategoryById = createSelector(
  [(state: RootState) => state.categories, (_, id: string) => id],
  (categories, id) => {
    const category = categories.find(category => category.id === id)
    return (
      category ?? {
        id: "",
        name: "",
        description: "",
        is_active: true,
        created_at: "",
        updated_at: null,
        deleted_at: null,
      }
    )
  },
)
