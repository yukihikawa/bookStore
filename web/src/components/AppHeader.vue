<template>
  <header class="sticky top-0 z-40 border-b border-slate-200 bg-white/95 backdrop-blur">
    <div class="container-bs flex h-14 items-center gap-6">
      <router-link to="/books" class="flex items-center gap-2">
        <span class="flex h-8 w-8 items-center justify-center rounded-lg bg-brand-500 text-base font-bold text-white">
          书
        </span>
        <span class="text-base font-semibold text-slate-900">网上书城</span>
      </router-link>

      <nav class="hidden items-center gap-1 text-sm md:flex">
        <router-link
          to="/books"
          class="rounded-md px-3 py-1.5 text-slate-600 transition hover:bg-slate-100 hover:text-slate-900"
        >
          书城
        </router-link>
        <router-link
          to="/cart"
          class="relative rounded-md px-3 py-1.5 text-slate-600 transition hover:bg-slate-100 hover:text-slate-900"
        >
          购物车
          <span
            v-if="cart.totalCount > 0"
            class="absolute -right-1 -top-1 flex h-4 min-w-4 items-center justify-center rounded-full bg-red-500 px-1 text-[10px] font-bold text-white"
          >
            {{ cart.totalCount }}
          </span>
        </router-link>
        <router-link
          v-if="auth.isLoggedIn"
          to="/orders"
          class="rounded-md px-3 py-1.5 text-slate-600 transition hover:bg-slate-100 hover:text-slate-900"
        >
          我的订单
        </router-link>
      </nav>

      <div class="ml-auto flex items-center gap-3 text-sm">
        <template v-if="auth.isLoggedIn">
          <span class="hidden text-slate-500 sm:inline">
            你好，
            <span class="font-medium text-slate-800">{{ auth.user?.username }}</span>
          </span>
          <router-link
            v-if="auth.isAdmin"
            to="/admin"
            class="rounded-md border border-slate-300 px-3 py-1.5 text-slate-600 transition hover:border-brand-400 hover:text-brand-600"
          >
            后台管理
          </router-link>
          <el-button size="small" text @click="onLogout">注销</el-button>
        </template>
        <template v-else>
          <router-link to="/login">登录</router-link>
          <el-button size="small" type="primary" @click="$router.push('/register')">
            注册
          </el-button>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const auth = useAuthStore()
const cart = useCartStore()

onMounted(() => {
  cart.refresh().catch(() => undefined)
  if (!auth.fetched && auth.token) auth.fetchMe().catch(() => undefined)
})

// 购物车数量变化时保持同步（进入相关页面时）
watch(
  () => route.fullPath,
  () => {
    if (route.name === 'books' || route.name === 'cart') {
      cart.refresh().catch(() => undefined)
    }
  }
)

async function onLogout() {
  await auth.logout()
}
</script>
