import { http, request } from './http'
import type { Order, OrderItem } from '@/types'

export const ordersApi = {
  myOrders: () => request<Order[]>(http.get('/api/orders/my')),

  myOrderItems: (orderId: string) =>
    request<OrderItem[]>(http.get(`/api/orders/my/${orderId}/items`)),

  receive: (orderId: string) =>
    request<{ message: string }>(http.post(`/api/orders/my/${orderId}/receive`)),

  allOrders: () => request<Order[]>(http.get('/api/orders/all')),

  send: (orderId: string) =>
    request<{ message: string }>(http.post(`/api/orders/${orderId}/send`)),

  allOrderItems: (orderId: string) =>
    request<OrderItem[]>(http.get(`/api/orders/${orderId}/items`)),
}
