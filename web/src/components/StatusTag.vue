<template>
  <el-tag :type="type" effect="light">{{ text }}</el-tag>
</template>

<script setup lang="ts">
import { computed } from 'vue'

/** 订单状态：0 未发货, 1 已发货, 2 已签收 */
const props = defineProps<{ status: number }>()

const map: Record<number, { text: string; type: 'info' | 'warning' | 'success' | 'danger' }> = {
  0: { text: '未发货', type: 'info' },
  1: { text: '已发货', type: 'warning' },
  2: { text: '已签收', type: 'success' },
}

const view = computed(() => map[props.status] ?? { text: `未知状态(${props.status})`, type: 'info' as const })
const text = computed(() => view.value.text)
const type = computed(() => view.value.type)
</script>
