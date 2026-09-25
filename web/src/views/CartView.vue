<template>
  <div>
    <AppHeader />
    <main class="container-bs py-6">
      <h1 class="mb-4 text-lg font-semibold text-slate-900">购物车</h1>

      <template v-if="cart.hasItems">
        <el-table :data="cart.data.items" class="w-full" style="width: 100%">
          <el-table-column prop="name" label="书名" min-width="180" show-overflow-tooltip />
          <el-table-column prop="price" label="单价" width="110" align="right">
            <template #default="{ row }">￥{{ row.price }}</template>
          </el-table-column>
          <el-table-column label="数量" width="170" align="center">
            <template #default="{ row }">
              <el-input-number
                :model-value="row.count"
                :min="1"
                :max="99"
                size="small"
                controls-position="right"
                :loading="actingId === row.id"
                @change="(v: number) => onCountChange(row, v)"
              />
            </template>
          </el-table-column>
          <el-table-column label="小计" width="120" align="right">
            <template #default="{ row }">
              <span class="font-medium text-red-500">￥{{ row.totalPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="110" align="center">
            <template #default="{ row }">
              <el-button
                size="small"
                type="danger"
                text
                :loading="actingId === row.id"
                @click="onRemove(row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="mt-6 flex items-center justify-end gap-6 rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
          <div class="text-sm text-slate-500">
            共 <span class="font-semibold text-slate-900">{{ cart.totalCount }}</span> 件商品
          </div>
          <div class="text-sm text-slate-500">
            合计：
            <span class="text-xl font-bold text-red-500">￥{{ cart.data.totalPrice }}</span>
          </div>
          <el-button type="danger" plain size="small" :loading="actingId === -1" @click="onClear">
            清空购物车
          </el-button>
          <el-button type="primary" :loading="checkingOut" @click="onCheckout">去结算</el-button>
        </div>
      </template>

      <el-empty v-else description="购物车是空的，去挑几本书吧" :image-size="110">
        <el-button type="primary" @click="$router.push('/books')">去逛逛</el-button>
      </el-empty>
    </main>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import { useCartStore } from '@/stores/cart'
import { useAuthStore } from '@/stores/auth'
import type { CartItem } from '@/types'

const router = useRouter()
const cart = useCartStore()
const auth = useAuthStore()

const actingId = ref<number | null>(null)
const checkingOut = ref(false)

onMounted(() => {
  cart.refresh().catch(() => undefined)
})

async function onCountChange(row: CartItem, value: number | undefined) {
  if (value == null || value < 1) {
    await cart.updateCount(row.id, 1)
    return
  }
  actingId.value = row.id
  try {
    await cart.updateCount(row.id, value)
  } finally {
    actingId.value = null
  }
}

async function onRemove(row: CartItem) {
  actingId.value = row.id
  try {
    await cart.remove(row.id)
    ElMessage.success('已删除购物车项')
  } finally {
    actingId.value = null
  }
}

async function onClear() {
  actingId.value = -1
  try {
    await cart.clear()
    ElMessage.success('购物车已清空')
  } finally {
    actingId.value = null
  }
}

async function onCheckout() {
  if (!(await auth.requireLogin())) return
  checkingOut.value = true
  try {
    const orderId = await cart.checkout()
    ElMessage.success('下单成功！')
    router.push(`/orders/${orderId}`)
  } finally {
    checkingOut.value = false
  }
}
</script>
