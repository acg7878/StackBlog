<template>
  <div class="tag-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>标签管理</span>
          <el-button type="primary" @click="handleAdd">新建标签</el-button>
        </div>
      </template>

      <!-- 标签表格 -->
      <el-table :data="tagList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="标签名称" width="200" />
        <el-table-column prop="description" label="描述" min-width="400" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入标签名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入标签描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { tagApi } from '@stackblog/shared'
import type { Tag } from '@stackblog/shared'

// 数据
const tagList = ref<Tag[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新建标签')
const formRef = ref<FormInstance>()

// 表单
const form = ref({
  id: 0,
  name: '',
  description: ''
})

// 表单验证规则
const rules: FormRules = {
  name: [
    { required: true, message: '请输入标签名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ]
}

// 加载标签列表
const loadTags = async () => {
  loading.value = true
  try {
    tagList.value = await tagApi.getList()
  } catch (error) {
    ElMessage.error('获取标签列表失败')
  } finally {
    loading.value = false
  }
}

// 新建标签
const handleAdd = () => {
  dialogTitle.value = '新建标签'
  form.value = {
    id: 0,
    name: '',
    description: ''
  }
  dialogVisible.value = true
}

// 编辑标签
const handleEdit = (row: Tag) => {
  dialogTitle.value = '编辑标签'
  form.value = { ...row }
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    try {
      if (form.value.id) {
        await tagApi.update(form.value.id, form.value)
        ElMessage.success('更新成功')
      } else {
        await tagApi.create(form.value)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      loadTags()
    } catch (error) {
      ElMessage.error('操作失败')
    }
  })
}

// 删除标签
const handleDelete = async (row: Tag) => {
  try {
    await ElMessageBox.confirm('确认删除该标签？删除后无法恢复！', '警告', {
      type: 'error'
    })
    await tagApi.delete(row.id)
    ElMessage.success('删除成功')
    loadTags()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadTags()
})
</script>

<style scoped lang="scss">
.tag-list {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
