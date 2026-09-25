import { http, request } from './http'
import type { Book, PageResult } from '@/types'

export const adminBooksApi = {
  page: (page: number, pageSize: number) =>
    request<PageResult<Book>>(http.get('/api/admin/books', { params: { page, pageSize } })),

  create: (book: Partial<Book>) => request<Book>(http.post('/api/admin/books', book)),

  update: (id: number, book: Partial<Book>) =>
    request<Book>(http.put(`/api/admin/books/${id}`, book)),

  remove: (id: number) => request<{ message: string }>(http.delete(`/api/admin/books/${id}`)),
}
