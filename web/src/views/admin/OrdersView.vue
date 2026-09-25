<template>
  <section class="rounded-xl border border-slate-200 bg-white shadow-sm">
    <div class="flex items-center justify-between border-b border-slate-100 p-4">
      <h2 class="text-base font-semibold text-slate-900">订单管理</h2>
      <el-button text :icon="Refresh" @click="load">刷新</el-button>
    </div>

    <el-table v-loading="loading" :data="orders" style="width: 100%">
      <el-table-column prop="orderId" label="订单号" min-width="200" show-overflow-tooltip />
      <el-table-column label="下单时间" width="170">
        <template #default="{ row }">{{ dayjs(row.createTime).format('YYYY-MM-DD HH:mm') }}</template>
      </el-table-column>
      <el-table-column label="金额" width="120" align="right">
        <template #default="{ row }">
          <span class="font-medium text-red-500">￥{{ row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="110" align="center">
        <template #default="{ row }">
          <StatusTag :status="row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template #default="{ row }">
          <el-button
            v-if="row.status === 0"
            size="small"
            type="primary"
            :loading="sendingId === row.orderId"
            @click="onSend(row)"
          >
            发货
          </el-button>
          <el-button
            size="small"
            text
            :loading="detailOrderId === row.orderId"
            @click="onDetail(row)"
          >
            明细
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="!loading && orders.length === 0" description="暂无订单" :image-size="100" />
  </section>

  <!-- 订单明细 -->
  <el-dialog v-model="dialogVisible" title="订单明细" width="560px" destroy-on-close>
    <el-table :data="detailItems" style="width: 100%">
      <el-table-column prop="name" label="书名" min-width="160" show-overflow-tooltip />
      <el-table-column prop="price" label="单价" width="90" align="right">
        <template #default="{ row }">￥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="count" label="数量" width="80" align="center" />
      <el-table-column label="小计" width="100" align="right">
        <template #default="{ row }">￥{{ row.totalPrice }}</template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import StatusTag from '@/components/StatusTag.vue'
import { ordersApi } from '@/api/orders'
import type { Order, OrderItem } from '@/types'

const orders = ref<Order[]>([])
const loading = ref(false)
const sendingId = ref<string | null>(null)

const dialogVisible = ref(false)
const detailOrderId = ref<string | null>(null)
const detailItems = ref<OrderItem[]>([])

async function load() {
  loading.value = true
  try {
    orders.value = await ordersApi.allOrders()
  } finally {
    loading.value = false
  }
}

async function onSend(row: Order) {
  sendingId.value = row.orderId
  try {
    await ordersApi.send(row.orderId)
    ElMessage.success('已发货')
    await load()
  } finally {
    sendingId.value = null
  }
}

async function onDetail(row: Order) {
  detailOrderId.value = row.orderId
  dialogVisible.value = true
  detailItems.value = await ordersApi.allOrderItems(row.orderId)
}

onMounted(() => {
  void load()
})
</script>
