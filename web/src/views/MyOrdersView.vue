<template>
  <div>
    <AppHeader />
    <main class="container-bs py-6">
      <h1 class="mb-4 text-lg font-semibold text-slate-900">我的订单</h1>

      <el-table v-if="orders.length > 0" :data="orders" style="width: 100%">
        <el-table-column label="订单号" min-width="200">
          <template #default="{ row }">
            <router-link :to="`/orders/${row.orderId}`" class="text-brand-600 hover:underline">
              {{ row.orderId }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="180">
          <template #default="{ row }">{{ fmtTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="金额" width="130" align="right">
          <template #default="{ row }">
            <span class="font-semibold text-red-500">￥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120" align="center">
          <template #default="{ row }">
            <StatusTag :status="row.status" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" align="center">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 1"
              size="small"
              type="primary"
              plain
              :loading="receivingId === row.orderId"
              @click="onReceive(row)"
            >
              确认收货
            </el-button>
            <el-button
              v-else
              size="small"
              text
              @click="$router.push(`/orders/${row.orderId}`)"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-else-if="!loading" description="还没有订单" :image-size="110">
        <el-button type="primary" @click="$router.push('/books')">去逛逛</el-button>
      </el-empty>
    </main>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import StatusTag from '@/components/StatusTag.vue'
import { ordersApi } from '@/api/orders'
import { useAuthStore } from '@/stores/auth'
import type { Order } from '@/types'

const orders = ref<Order[]>([])
const loading = ref(false)
const receivingId = ref<string | null>(null)

const auth = useAuthStore()

function fmtTime(t: string) {
  return dayjs(t).format('YYYY-MM-DD HH:mm')
}

async function load() {
  loading.value = true
  try {
    orders.value = await ordersApi.myOrders()
  } finally {
    loading.value = false
  }
}

async function onReceive(row: Order) {
  receivingId.value = row.orderId
  try {
    await ordersApi.receive(row.orderId)
    ElMessage.success('已签收，感谢购买！')
    await load()
  } finally {
    receivingId.value = null
  }
}

onMounted(async () => {
  if (!(await auth.requireLogin())) return
  void load()
})
</script>
