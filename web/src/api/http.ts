import axios from 'axios'
import type { AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiError } from '@/types'

/**
 * SPA 部署位置：<contextPath>/app/**（静态产物在 webapp/app/，Tomcat 原生托管）。
 * apiBase() 从当前页面路径推断 <contextPath>（可能为空），
 * axios / router / 验证码图片均基于它拼接：
 *   - 开发模式（vite dev server）：返回 ''，请求走绝对路径 /api，由 vite 代理到后端
 *   - 生产模式（Tomcat）：页面 URL 形如 <context>/app/books，推断出 <context>
 */
export function apiBase(): string {
  // 开发模式：vite 已把 /api 与 /kaptcha.jpg 代理到后端
  if (import.meta.env.DEV) return ''
  if (typeof location === 'undefined') return ''
  const m = location.pathname.match(/^(.*)\/app(?:\/|$)/)
  return m ? m[1] : ''
}

/**
 * 统一 axios 实例
 * - 同源部署：浏览器自动携带 JSESSIONID（session 登录态）
 */
export const http = axios.create({
  baseURL: apiBase(),
  timeout: 15000,
})

let warnedAuthRedirect = false

/** 业务/HTTP 统一错误出口 */
export function handleError(err: unknown): void {
  if (axios.isAxiosError(err)) {
    const status = err.response?.status
    const data = err.response?.data as ApiError | undefined

    if (status === 401 || data?.code === '10001') {
      // 会话过期：交给调用方（store）决定跳登录
      if (!warnedAuthRedirect) {
        console.warn('[auth] session expired')
        warnedAuthRedirect = true
      }
      return
    }
    ElMessage.error(data?.message || err.message || '网络异常，请稍后重试')
  } else {
    ElMessage.error('发生未知错误')
  }
}

export function isAuthError(err: unknown): boolean {
  if (!axios.isAxiosError(err)) return false
  const status = err.response?.status
  const data = err.response?.data as ApiError | undefined
  return status === 401 || data?.code === '10001'
}

/** 供 pinia store 使用的封装：失败时提示且抛出错误 */
export async function request<T>(promise: Promise<AxiosResponse<T>>): Promise<T> {
  try {
    const res = await promise
    return res.data
  } catch (err) {
    handleError(err)
    throw err
  }
}
