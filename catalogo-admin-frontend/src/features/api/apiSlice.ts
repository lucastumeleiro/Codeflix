import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query"

const baseUrl = "http://localhost:8000/api"

export const apiSlice = createApi({
  reducerPath: "api",
  tagTypes: ["Categories"],
  baseQuery: fetchBaseQuery({ baseUrl: baseUrl }),
  endpoints: builder => ({}),
})
