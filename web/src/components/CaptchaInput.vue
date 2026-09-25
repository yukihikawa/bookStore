<template>
  <el-form-item label="验证码">
    <div class="flex items-center gap-2">
      <el-input
        v-model="code"
        placeholder="请输入验证码"
        autocomplete="off"
        class="w-36"
        @keyup.enter="$emit('enter')"
      >
        <template #suffix>
          <span class="text-xs text-slate-400" v-if="refreshing">…</span>
        </template>
      </el-input>
      <button
        type="button"
        class="shrink-0 overflow-hidden rounded border border-slate-300 bg-white"
        :title="'看不清？点击图片刷新（第 ' + round + ' 次）'"
        @click="refresh"
      >
        <!-- kaptcha 原图为 200x50（4:1）；容器同样按 4:1（40x160）且 object-contain，
             避免纵向拉伸导致字符变形 -->
        <img :src="src" alt="图形验证码" class="block h-10 w-40 select-none object-contain" draggable="false" />
      </button>
      <span class="text-xs text-slate-400">点击图片刷新</span>
    </div>
  </el-form-item>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { authApi } from '@/api/auth'
import { apiBase } from '@/api/http'

/**
 * 图形验证码输入组件
 * /kaptcha.jpg 必须由浏览器直接请求（同源），以便 KaptchaServlet 在同一 session 种下校验 token；
 * 点击刷新仅改 cache-busting 参数。
 */
const props = defineProps<{
  modelValue: string
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
  (e: 'enter'): void
}>()

const code = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v),
})

const round = ref(0)
const refreshing = ref(false)
// 每次取新的时间戳，避免浏览器把相邻两次验证码请求当作同一资源缓存
const src = computed(() => `${apiBase()}/kaptcha.jpg?t=${Date.now()}-${round.value}`)

onMounted(() => {
  void authApi.captchaRefreshUrl().catch(() => undefined)
})

function refresh() {
  refreshing.value = true
  round.value += 1
  emit('update:modelValue', '')
  // 给新图片一个加载回调后解除 loading
  window.setTimeout(() => {
    refreshing.value = false
  }, 300)
}
</script>
