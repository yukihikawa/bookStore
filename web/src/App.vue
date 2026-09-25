<template>
  <router-view />
</template>

<script setup lang="ts">
import { watchEffect } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const auth = useAuthStore()

// 页面标题
watchEffect(() => {
  const t = route.meta.title as string | undefined
  document.title = t ? `${t} · 网上书城` : '网上书城'
})

// 尝试恢复会话（持久 Cookie 保持登录态时有效）——不阻塞渲染
auth.ensureLoaded().catch(() => undefined)
</script>
