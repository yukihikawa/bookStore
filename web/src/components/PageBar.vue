<template>
  <div class="flex items-center justify-center py-6">
    <el-pagination
      v-if="pageTotal > 1"
      :current-page="pageNo"
      :page-size="pageSize"
      :total="pageTotalCount"
      layout="prev, pager, next, jumper"
      background
      @current-change="onPage"
    />
  </div>
</template>

<script setup lang="ts">
withDefaults(
  defineProps<{
    pageNo: number
    pageSize: number
    pageTotal: number
    pageTotalCount: number
  }>(),
  {}
)

const emit = defineEmits<{
  (e: 'page-change', page: number): void
}>()

function onPage(page: number) {
  emit('page-change', page)
}
</script>
