<template>
  <div>
    <AppHeader />
    <main class="container-bs py-6">
      <div class="mb-4 flex items-center gap-3">
        <el-button text :icon="ArrowLeft" @click="$router.push('/orders')">返回订单列表</el-button>
        <h1 class="text-lg font-semibold text-slate-900">订单详情</h1>
      </div>

      <template v-if="order">
        <el-descriptions :column="2" border class="mb-4">
          <el-descriptions-item label="订单号">{{ order.orderId }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">
            {{ dayjs(order.createTime).format('YYYY-MM-DD HH:mm:ss') }}
          </el-descriptions-item>
          <el-descriptions-item label="订单金额">
            <span class="font-semibold text-red-500">￥{{ order.price }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <StatusTag :status="order.status" />
          </el-descriptions-item>
        </el-descriptions>

        <el-table :data="items" style="width: 100%">
          <el-table-column prop="name" label="书名" min-width="200" show-overflow-tooltip />
          <el-table-column prop="price" label="单价" width="120" align="right">
            <template #default="{ row }">￥{{ row.price }}</template>
          </el-table-column>
          <el-table-column prop="count" label="数量" width="100" align="center" />
          <el-table-column label="小计" width="140" align="right">
            <template #default="{ row }">
              <span class="font-medium text-red-500">￥{{ row.totalPrice }}</span>
            </template>
          </el-table-column>
        </el-table>
      </template>

      <el-empty v-else-if="!loading" description="订单不存在或无权查看" :image-size="110">
        <el-button type="primary" @click="$router.push('/orders')">返回订单列表</el-button>
      </el-empty>
    </main>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import StatusTag from '@/components/StatusTag.vue'
import { ordersApi } from '@/api/orders'
import { useAuthStore } from '@/stores/auth'
import type { Order, OrderItem } from '@/types'

const route = useRoute()
const auth = useAuthStore()

const orderId = route.params.orderId as string
const order = ref<Order | null>(null)
const items = ref<OrderItem[]>([])
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    const list = await ordersApi.myOrders()
    order.value = list.find((o) => o.orderId === orderId) ?? null
    if (order.value) {
      items.value = await ordersApi.myOrderItems(orderId)
    }
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  if (!(await auth.requireLogin())) return
  void load()
})
</script>
