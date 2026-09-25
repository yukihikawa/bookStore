import { http, request } from './http'
import type { UserInfo } from '@/types'

export interface AuthResult {
  token: string
  user: UserInfo
}

interface CaptchaInfo {
  captchaUrl: string
}

/** 登录成功时返回 token；onUnauthorized 由调用方传入（避免循环依赖 router） */
export const authApi = {
  login: (username: string, password: string, code: string) =>
    request<AuthResult>(http.post('/api/auth/login', { username, password, code })),

  register: (username: string, password: string, email: string, code: string) =>
    request<AuthResult>(http.post('/api/auth/register', { username, password, email, code })),

  me: () => request<{ user: UserInfo }>(http.get('/api/auth/me')),

  logout: () => request<{ message: string }>(http.post('/api/auth/logout')),

  captchaRefreshUrl: () =>
    http.get<CaptchaInfo>('/api/auth/captcha').then((r) => r.data) as Promise<CaptchaInfo>,
}
