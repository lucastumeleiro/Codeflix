export type TResults = {
  data: TCategory[]
  links: TLink[]
  meta: TMeta
}

export type TResult = {
  data: TCategory
  meta: TMeta
  links: TLink[]
}

export type TCategory = {
  id: string
  name: string
  description?: string | null
  is_active: boolean
  created_at: string
  updated_at?: string | null
  deleted_at?: string | null
}

export type TMeta = {
  to: number
  from: number
  path: string
  total: number
  per_page: number
  last_page: number
  current_page: number
}

export type TLink = {
  prev: null
  last: string
  next: string
  first: string
}

export type TCategoryParams = {
  page?: number
  per_page?: number
  search?: string
  is_active?: boolean
}
