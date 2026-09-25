import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '@/api/auth'
import type { UserInfo } from '@/types'
import { ElMessage } from 'element-plus'

/**
 * 登录态 store：
 * 后端为 session 登录，token 仅用于前端记忆"本会话已登录"，
 * 刷新后通过 /api/auth/me 恢复真实会话状态。
 */
export const useAuthStore = defineStore('auth', () => {
  const router = useRouter()
  const user = ref<UserInfo | null>(null)
  const token = ref(sessionStorage.getItem('bs_token') || '')
  const fetched = ref(false)

  const isLoggedIn = computed(() => user.value !== null)
  const isAdmin = computed(() => user.value?.admin === true)

  /** 从后端拉取当前会话用户；401 时清空本地状态 */
  async function fetchMe() {
    try {
      const { user: u } = await authApi.me()
      user.value = u
      token.value = sessionStorage.getItem('bs_token') || ''
    } catch (err) {
      user.value = null
      token.value = ''
      sessionStorage.removeItem('bs_token')
    } finally {
      fetched.value = true
    }
  }

  async function login(username: string, password: string, code: string) {
    const res = await authApi.login(username, password, code)
    user.value = res.user
    token.value = res.token
    sessionStorage.setItem('bs_token', res.token)
    ElMessage.success(`欢迎回来，${res.user.username}`)
    return res.user
  }

  async function register(username: string, password: string, email: string, code: string) {
    const res = await authApi.register(username, password, email, code)
    user.value = res.user
    token.value = res.token
    sessionStorage.setItem('bs_token', res.token)
    ElMessage.success(`注册成功，已自动登录为 ${res.user.username}`)
    return res.user
  }

  async function logout() {
    try {
      await authApi.logout()
    } finally {
      user.value = null
      token.value = ''
      sessionStorage.removeItem('bs_token')
      router.push('/books')
      ElMessage.success('已退出登录')
    }
  }

  async function requireLogin(): Promise<boolean> {
    if (!fetched.value) {
      if (token.value) {
        await fetchMe()
      } else {
        fetched.value = true
      }
    }
    if (!user.value) {
      router.push({ path: '/login', query: { redirect: router.currentRoute.value.fullPath } })
      return false
    }
    return true
  }

  async function ensureLoaded(): Promise<void> {
    // 有 token 或曾拉取过就跳过；否则（直接刷新页面）拉一次判断真实会话
    if (fetched.value) return
    if (!token.value) {
      fetched.value = true
      return
    }
    await fetchMe()
  }

  return {
    user,
    token,
    fetched,
    isLoggedIn,
    isAdmin,
    fetchMe,
    login,
    register,
    logout,
    requireLogin,
    ensureLoaded,
  }
})
