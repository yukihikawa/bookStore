import { http, request } from './http'
import type { CartView } from '@/types'

export const cartApi = {
  list: () => request<CartView>(http.get('/api/cart')),

  add: (id: number) => request<CartView>(http.post('/api/cart/items', { id })),

  updateCount: (id: number, count: number) =>
    request<CartView>(http.put(`/api/cart/items/${id}`, { count })),

  remove: (id: number) => request<CartView>(http.delete(`/api/cart/items/${id}`)),

  clear: () => request<CartView>(http.delete('/api/cart')),

  checkout: () => request<{ orderId: string; cart: CartView }>(http.post('/api/cart/checkout')),
}
