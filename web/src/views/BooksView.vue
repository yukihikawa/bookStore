<template>
  <div>
    <AppHeader />
    <main class="container-bs py-6">
      <!-- 价格筛选 -->
      <section class="mb-4 flex flex-col gap-3 rounded-xl border border-slate-200 bg-white p-4 shadow-sm sm:flex-row sm:items-end">
        <div class="flex items-center gap-2 text-sm">
          <span class="shrink-0 text-slate-500">价格区间（元）</span>
          <el-input v-model.number="min" placeholder="最低" clearable class="w-28" @keyup.enter="search" />
          <span class="text-slate-400">-</span>
          <el-input v-model.number="max" placeholder="最高" clearable class="w-28" @keyup.enter="search" />
          <el-button type="primary" :loading="loading" @click="search">
            <el-icon class="mr-1"><Search /></el-icon>查询
          </el-button>
          <el-button v-if="min !== '' || max !== ''" @click="reset">重置</el-button>
        </div>

        <div class="sm:ml-auto sm:text-right">
          <el-tag v-if="cart.lastAdded" type="success" effect="light">
            刚刚将「{{ cart.lastAdded }}」加入购物车
          </el-tag>
          <el-tag v-else-if="!cart.hasItems" type="info" effect="light">当前购物车为空</el-tag>
          <el-tag v-else type="primary" effect="light">购物车 {{ cart.totalCount }} 件商品</el-tag>
        </div>
      </section>

      <!-- 图书列表 -->
      <section class="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        <div
          v-for="book in books"
          :key="book.id"
          class="group flex gap-4 rounded-xl border border-slate-200 bg-white p-4 shadow-sm transition hover:shadow-md"
        >
          <div class="flex h-24 w-16 shrink-0 items-center justify-center rounded-md bg-brand-50 text-brand-500">
            <el-icon :size="32"><Reading /></el-icon>
          </div>
          <div class="flex min-w-0 flex-1 flex-col">
            <h3 class="truncate font-semibold text-slate-900" :title="book.name">
              {{ book.name }}
            </h3>
            <p class="mt-0.5 truncate text-sm text-slate-500">{{ book.author }}</p>
            <div class="mt-1 flex items-center gap-3 text-xs text-slate-400">
              <span>库存 {{ book.stock }}</span>
              <span>销量 {{ book.sales }}</span>
            </div>
            <div class="mt-auto flex items-center justify-between pt-2">
              <span class="text-lg font-semibold text-red-500">￥{{ book.price }}</span>
              <el-button
                v-if="auth.isLoggedIn"
                size="small"
                type="primary"
                plain
                :loading="addingId === book.id"
                :disabled="book.stock < 1"
                @click="addToCart(book)"
              >
                加入购物车
              </el-button>
              <el-text v-else size="small" type="info">登录后可加购</el-text>
            </div>
          </div>
        </div>

        <div v-if="!loading && books.length === 0" class="col-span-full py-16 text-center text-slate-400">
          <el-empty description="该价格区间内暂无书籍" />
        </div>
      </section>

      <PageBar
        :page-no="pageNo"
        :page-size="pageSize"
        :page-total="pageTotal"
        :page-total-count="pageTotalCount"
        @page-change="onPageChange"
      />
    </main>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, Reading } from '@element-plus/icons-vue'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import PageBar from '@/components/PageBar.vue'
import { booksApi } from '@/api/books'
import { useCartStore } from '@/stores/cart'
import { useAuthStore } from '@/stores/auth'
import type { Book } from '@/types'

const route = useRoute()
const auth = useAuthStore()
const cart = useCartStore()

const books = ref<Book[]>([])
const loading = ref(false)
const pageNo = ref(1)
const pageSize = ref(12)
const min = ref<number | ''>('')
const max = ref<number | ''>('')
const pageTotal = ref(0)
const pageTotalCount = ref(0)
const addingId = ref<number | null>(null)

async function load() {
  const { max: fmax } = route.query
  if (fmax != null) max.value = Number(fmax)
  loading.value = true
  try {
    const page = await booksApi.page({
      min: min.value === '' ? undefined : min.value,
      max: max.value === '' ? undefined : max.value,
      page: pageNo.value,
      pageSize: pageSize.value,
    })
    books.value = page.items
    pageNo.value = page.pageNo
    pageTotal.value = page.pageTotal
    pageTotalCount.value = page.pageTotalCount
  } finally {
    loading.value = false
  }
}

function search() {
  pageNo.value = 1
  void load()
}

function reset() {
  min.value = ''
  max.value = ''
  pageNo.value = 1
  void load()
}

function onPageChange(page: number) {
  pageNo.value = page
  void load()
}

async function addToCart(book: Book) {
  if (!auth.isLoggedIn) {
    await auth.requireLogin()
    return
  }
  addingId.value = book.id
  try {
    await cart.add(book.id)
    ElMessage.success(`「${book.name}」已加入购物车`)
  } finally {
    addingId.value = null
  }
}

onMounted(() => {
  void load()
  void auth.ensureLoaded()
})
</script>
