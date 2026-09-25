<template>
  <section class="rounded-xl border border-slate-200 bg-white shadow-sm">
    <!-- 工具栏 -->
    <div class="flex items-center justify-between border-b border-slate-100 p-4">
      <h2 class="text-base font-semibold text-slate-900">书籍管理</h2>
      <el-button type="primary" :icon="Plus" @click="openEditor(null)">添加书籍</el-button>
    </div>

    <!-- 表格 -->
    <el-table v-loading="loading" :data="books" style="width: 100%">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="name" label="书名" min-width="160" show-overflow-tooltip />
      <el-table-column prop="author" label="作者" width="140" show-overflow-tooltip />
      <el-table-column prop="price" label="价格（元）" width="110" align="right">
        <template #default="{ row }">￥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="sales" label="销量" width="90" align="center" />
      <el-table-column prop="stock" label="库存" width="90" align="center" />
      <el-table-column label="操作" width="160" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" text @click="openEditor(row)">编辑</el-button>
          <el-button
            size="small"
            type="danger"
            text
            :loading="deletingId === row.id"
            @click="onRemove(row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="border-t border-slate-100 p-3">
      <PageBar
        :page-no="pageNo"
        :page-size="pageSize"
        :page-total="pageTotal"
        :page-total-count="pageTotalCount"
        @page-change="onPageChange"
      />
    </div>
  </section>

  <!-- 新增/编辑对话框 -->
  <el-dialog
    v-model="editorVisible"
    :title="editingId ? '编辑书籍' : '添加书籍'"
    width="480px"
    destroy-on-close
  >
    <el-form ref="editorRef" :model="form" :rules="rules" label-position="top">
      <el-form-item label="书名" prop="name">
        <el-input v-model.trim="form.name" placeholder="请输入书名" />
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input v-model.trim="form.author" placeholder="请输入作者" />
      </el-form-item>
      <el-form-item label="价格（元）" prop="price">
        <el-input-number
          v-model="form.price"
          :min="0.01"
          :precision="2"
          :step="1"
          class="!w-full"
        />
      </el-form-item>
      <el-form-item label="库存" prop="stock">
        <el-input-number v-model="form.stock" :min="0" :step="1" class="!w-full" />
      </el-form-item>
      <el-form-item v-if="editingId" label="销量（不可修改，仅展示）">
        <el-input :model-value="form.sales" disabled class="!w-full" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="editorVisible = false">取消</el-button>
      <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import PageBar from '@/components/PageBar.vue'
import { adminBooksApi } from '@/api/adminBooks'
import type { Book } from '@/types'

const books = ref<Book[]>([])
const loading = ref(false)
const pageNo = ref(1)
const pageSize = ref(10)
const pageTotal = ref(0)
const pageTotalCount = ref(0)
const deletingId = ref<number | null>(null)

// 编辑对话框
const editorVisible = ref(false)
const saving = ref(false)
const editingId = ref<number | null>(null)
const editorRef = ref<FormInstance>()
const form = reactive<Partial<Book>>({
  name: '',
  author: '',
  price: 0,
  stock: 0,
  sales: 0,
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'change' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'change' }],
}

async function load() {
  loading.value = true
  try {
    const page = await adminBooksApi.page(pageNo.value, pageSize.value)
    books.value = page.items
    pageNo.value = page.pageNo
    pageTotal.value = page.pageTotal
    pageTotalCount.value = page.pageTotalCount
  } finally {
    loading.value = false
  }
}

function onPageChange(p: number) {
  pageNo.value = p
  void load()
}

function openEditor(book: Book | null) {
  editingId.value = book?.id ?? null
  form.name = book?.name ?? ''
  form.author = book?.author ?? ''
  form.price = book?.price ?? 0
  form.stock = book?.stock ?? 0
  form.sales = book?.sales ?? 0
  editorVisible.value = true
}

async function onSave() {
  const valid = await editorRef.value?.validate().catch(() => false)
  if (!valid) return

  saving.value = true
  try {
    if (editingId.value == null) {
      await adminBooksApi.create({
        name: form.name,
        author: form.author,
        price: form.price,
        stock: form.stock,
        sales: 0,
      })
      ElMessage.success('添加成功')
    } else {
      await adminBooksApi.update(editingId.value, {
        name: form.name,
        author: form.author,
        price: form.price,
        stock: form.stock,
      })
      ElMessage.success('保存成功')
    }
    editorVisible.value = false
    await load()
  } finally {
    saving.value = false
  }
}

async function onRemove(row: Book) {
  try {
    await ElMessageBox.confirm(`确定删除书籍「${row.name}」吗？`, '删除确认', {
      type: 'warning',
    })
  } catch {
    return
  }
  deletingId.value = row.id
  try {
    await adminBooksApi.remove(row.id)
    ElMessage.success('已删除')
    await load()
  } finally {
    deletingId.value = null
  }
}

onMounted(() => {
  void load()
})
</script>
