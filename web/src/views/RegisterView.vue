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
            <el-input v-model.trim="form.username" placeholder="4-20 位字母/数字" autocomplete="username" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="6-20 位"
              show-password
              autocomplete="new-password"
            />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model.trim="form.email" placeholder="选填" autocomplete="email" />
          </el-form-item>
          <CaptchaInput v-model="form.code" @enter="submit" />
        </el-form>

        <el-button type="primary" class="mt-4 w-full" :loading="submitting" @click="submit">
          注册
        </el-button>

        <div class="mt-4 text-center text-xs text-slate-400">
          已有账号？
          <router-link to="/login" class="text-brand-600 hover:underline">直接登录</router-link>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import CaptchaInput from '@/components/CaptchaInput.vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const auth = useAuthStore()

const formRef = ref<FormInstance>()
const submitting = ref(false)
const form = reactive({
  username: '',
  password: '',
  email: '',
  code: '',
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 20, message: '用户名长度 4-20 位', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度 6-20 位', trigger: 'blur' },
  ],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
}

async function submit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    await auth.register(form.username, form.password, form.email, form.code)
    router.replace('/books')
  } finally {
    submitting.value = false
  }
}
</script>
