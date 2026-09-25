<template>
  <div class="flex min-h-screen items-center justify-center bg-slate-50 px-4">
    <div class="w-full max-w-sm">
      <div class="mb-6 text-center">
        <router-link to="/books" class="flex items-center justify-center gap-2">
          <span class="flex h-10 w-10 items-center justify-center rounded-xl bg-brand-500 text-lg font-bold text-white">
            书
          </span>
          <span class="text-xl font-semibold text-slate-900">网上书城</span>
        </router-link>
      </div>

      <el-card class="border-0 shadow-sm">
        <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent>
          <el-form-item label="用户名" prop="username">
            <el-input v-model.trim="form.username" placeholder="请输入用户名" autocomplete="username" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              show-password
              autocomplete="current-password"
            />
          </el-form-item>
          <CaptchaInput v-model="form.code" @enter="submit" />
        </el-form>

        <el-button
          type="primary"
          class="mt-4 w-full"
          :loading="submitting"
          @click="submit"
        >
          登录
        </el-button>

        <div class="mt-4 text-center text-xs text-slate-400">
          还没有账号？
          <router-link to="/register" class="text-brand-600 hover:underline">立即注册</router-link>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import CaptchaInput from '@/components/CaptchaInput.vue'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const formRef = ref<FormInstance>()
const submitting = ref(false)
const form = reactive({
  username: '',
  password: '',
  code: '',
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
}

onMounted(() => {
  // 已登录用户到达登录页时回首页（保证金守已在路由层做过，这里兜底一次会话探测）
  void auth.ensureLoaded()
})

function redirectAfter() {
  const target = route.query.redirect
  if (typeof target === 'string' && target.startsWith('/')) {
    router.replace(target)
    return
  }
  router.replace('/books')
}

async function submit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const user = await auth.login(form.username, form.password, form.code)
    // 若目标为后台页面但当前账号非管理员，守卫会将其挡回书籍页
    const target = route.query.redirect
    if (typeof target === 'string' && target.startsWith('/admin') && !user.admin) {
      router.replace(target) // 交给守卫统一处理
      return
    }
    redirectAfter()
  } catch {
    // 业务错误（密码错 / 验证码错 / 用户存在）已由 axios 拦截器统一提示
  } finally {
    submitting.value = false
  }
}
</script>
