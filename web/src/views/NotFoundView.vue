<template>
  <div class="flex min-h-screen items-center justify-center bg-slate-50">
    <div class="text-center">
      <div class="text-6xl font-bold text-brand-500">404</div>
      <p class="mt-4 text-slate-500">页面不存在或已被移除</p>
      <el-button class="mt-6" type="primary" @click="$router.replace('/books')">回到书城</el-button>
    </div>
  </div>
</template>
