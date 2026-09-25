import { http, request } from './http'
import type { Book, PageResult } from '@/types'

export const booksApi = {
  page: (params: { min?: number | ''; max?: number | ''; page?: number; pageSize?: number }) =>
    request<PageResult<Book>>(http.get('/api/books', { params })),

  detail: (id: number) => request<Book>(http.get(`/api/books/${id}`)),
}
