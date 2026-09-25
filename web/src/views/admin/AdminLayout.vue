<template>
  <div class="flex min-h-screen bg-slate-100">
    <aside class="hidden w-52 shrink-0 border-r border-slate-200 bg-white md:block">
      <div class="flex h-14 items-center gap-2 border-b border-slate-200 px-4">
        <span class="flex h-8 w-8 items-center justify-center rounded-lg bg-brand-500 text-base font-bold text-white">
          书
        </span>
        <div>
          <div class="text-sm font-semibold text-slate-900">书城后台</div>
          <div class="text-xs text-slate-400">{{ auth.user?.username }}</div>
        </div>
      </div>
      <el-menu
        :default-active="$route.path"
        class="border-r-0"
        router
      >
        <el-menu-item index="/admin/books">
          <el-icon><Notebook /></el-icon>
          <span>书籍管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/orders">
          <el-icon><List /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
      </el-menu>
      <div class="border-t border-slate-200 p-3">
        <el-button size="small" class="w-full" @click="goStore">进入书城前台</el-button>
        <el-button size="small" class="mt-2 w-full" type="danger" plain @click="onLogout">
          注销
        </el-button>
      </div>
    </aside>

    <div class="flex min-w-0 flex-1 flex-col">
      <header class="flex h-14 items-center justify-between border-b border-slate-200 bg-white px-4 md:px-6">
        <div class="md:hidden text-sm font-semibold">书城后台</div>
        <nav class="flex gap-2 md:hidden">
          <el-button size="small" type="primary" plain :round="true" @click="goStore">前台</el-button>
          <el-button size="small" type="danger" plain @click="onLogout">注销</el-button>
        </nav>
        <div class="hidden items-center gap-3 text-sm text-slate-500 md:flex">
          网上书城 · 管理端
        </div>
      </header>

      <div class="flex-1 p-4 md:p-6">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { Notebook, List } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const auth = useAuthStore()

function goStore() {
  router.push('/books')
}

function onLogout() {
  void auth.logout()
}
</script>
