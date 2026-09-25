/** 与后端实体对应的 TypeScript 类型 */

export interface Book {
  id: number
  name: string
  author: string
  price: number
  sales: number
  stock: number
  imgPath?: string
}

export interface CartItem {
  id: number
  name: string
  count: number
  price: number
  totalPrice: number
}

export interface CartView {
  totalCount: number
  totalPrice: number
  items: CartItem[]
}

export interface PageResult<T> {
  items: T[]
  pageNo: number
  pageSize: number
  pageTotal: number
  pageTotalCount: number
}

export interface Order {
  orderId: string
  createTime: string
  price: number
  /** 0: 未发货, 1: 已发货, 2: 已签收 */
  status: number
  userId: number
}

export interface OrderItem {
  id: number
  name: string
  count: number
  price: number
  totalPrice: number
  orderId: string
}

export interface UserInfo {
  id: number
  username: string
  email: string
  admin: boolean
}

export interface ApiError {
  code: string
  message: string
}
