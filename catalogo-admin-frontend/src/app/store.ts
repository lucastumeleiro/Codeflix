import type { Action, ThunkAction } from "@reduxjs/toolkit"
import { configureStore } from "@reduxjs/toolkit"
import {
  categoriesApiSlice,
  categoriesSlice,
} from "../features/categories/categoriesSlice"

export const store = configureStore({
  reducer: {
    categories: categoriesSlice.reducer,
    [categoriesApiSlice.reducerPath]: categoriesApiSlice.reducer,
  },
})

export type AppStore = typeof store
export type AppDispatch = typeof store.dispatch
export type RootState = ReturnType<typeof store.getState>
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action
>
