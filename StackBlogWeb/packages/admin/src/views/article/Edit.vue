<template>
  <div class="article-edit-container">
    <el-page-header @back="router.back()" :title="isEdit ? '编辑文章' : '创建文章'">
      <template #content>
        <span class="page-title">{{ isEdit ? '编辑文章' : '创建文章' }}</span>
      </template>
      <template #extra>
        <el-button @click="router.back()">取消</el-button>
        <el-button type="primary" @click="handleSaveDraft" :loading="saving">保存草稿</el-button>
        <el-button type="success" @click="handlePublish" :loading="saving">发布</el-button>
      </template>
    </el-page-header>

    <el-card shadow="never" class="form-card">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="文章标题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="请输入文章标题"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="文章内容" prop="contentMd">
          <MdEditor
            v-model="form.contentMd"
            :preview="true"
            :toolbars="toolbars"
            :placeholder="'请输入Markdown格式的文章内容'"
            style="height: 600px"
            @onSave="handleSaveDraft"
            @onUploadImg="handleUploadImg"
          />
        </el-form-item>

        <el-form-item label="文章摘要" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入文章摘要（选填）"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="文章分类">
              <el-select v-model="form.typeId" placeholder="请选择分类" clearable>
                <el-option label="技术" :value="1" />
                <el-option label="生活" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="文章标签">
              <el-select v-model="form.tags" multiple placeholder="请选择标签" clearable>
                <el-option label="Vue" :value="1" />
                <el-option label="Kotlin" :value="2" />
                <el-option label="Spring Boot" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="文章设置">
          <el-checkbox v-model="form.top">置顶</el-checkbox>
          <el-checkbox v-model="form.recommended">推荐</el-checkbox>
          <el-checkbox v-model="form.original">原创</el-checkbox>
          <el-checkbox v-model="form.comment">允许评论</el-checkbox>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { articleApi } from '@shared/api'
import type { ArticleDTO } from '@shared/types'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)
const articleId = computed(() => Number(route.params.id))

const formRef = ref<FormInstance>()
const saving = ref(false)

const form = reactive<ArticleDTO>({
  title: '',
  content: '',
  contentMd: '',
  description: '',
  coverImage: '',
  typeId: undefined,
  tags: [],
  top: false,
  recommended: false,
  original: true,
  comment: true,
  status: 0
})

const rules: FormRules = {
  title: [
    { required: true, message: '请输入文章标题', trigger: 'blur' }
  ],
  contentMd: [
    { required: true, message: '请输入文章内容', trigger: 'blur' }
  ]
}

// Markdown编辑器工具栏配置
const toolbars = [
  'bold',
  'underline',
  'italic',
  'strikeThrough',
  '-',
  'title',
  'sub',
  'sup',
  'quote',
  'unorderedList',
  'orderedList',
  'task',
  '-',
  'codeRow',
  'code',
  'link',
  'image',
  'table',
  '-',
  'revoke',
  'next',
  'save',
  '=',
  'pageFullscreen',
  'fullscreen',
  'preview',
  'htmlPreview',
  'catalog'
]

// 处理图片上传
const handleUploadImg = async (files: File[], callback: (urls: string[]) => void) => {
  // TODO: 实现图片上传到服务器
  // 临时方案：使用 base64
  const urls: string[] = []
  for (const file of files) {
    const reader = new FileReader()
    reader.onload = (e) => {
      urls.push(e.target?.result as string)
      if (urls.length === files.length) {
        callback(urls)
      }
    }
    reader.readAsDataURL(file)
  }
}

// 保存草稿
const handleSaveDraft = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    saving.value = true
    form.status = 0
    form.content = form.contentMd // 简单处理，实际应该转换为HTML

    try {
      if (isEdit.value) {
        await articleApi.update(articleId.value, form)
        ElMessage.success('保存成功')
      } else {
        await articleApi.create(form)
        ElMessage.success('保存成功')
        router.back()
      }
    } catch (error: any) {
      ElMessage.error(error.message || '保存失败')
    } finally {
      saving.value = false
    }
  })
}

// 发布
const handlePublish = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    saving.value = true
    form.status = 1
    form.content = form.contentMd

    try {
      if (isEdit.value) {
        await articleApi.update(articleId.value, form)
        ElMessage.success('发布成功')
      } else {
        await articleApi.create(form)
        ElMessage.success('发布成功')
        router.back()
      }
    } catch (error: any) {
      ElMessage.error(error.message || '发布失败')
    } finally {
      saving.value = false
    }
  })
}

// 加载文章详情
const loadArticleDetail = async () => {
  try {
    const article = await articleApi.getDetail(articleId.value)
    Object.assign(form, {
      title: article.title,
      content: article.content || '',
      contentMd: article.contentMd || '',
      description: article.description || '',
      coverImage: article.coverImage || '',
      typeId: article.typeId,
      tags: article.tags?.map(t => t.id) || [],
      top: article.top,
      recommended: article.recommended,
      original: article.original,
      comment: article.comment,
      status: article.status
    })
  } catch (error: any) {
    ElMessage.error(error.message || '加载文章失败')
  }
}

onMounted(() => {
  if (isEdit.value) {
    loadArticleDetail()
  }
})
</script>

<style scoped lang="scss">
.article-edit-container {
  .page-title {
    font-size: 18px;
    font-weight: 500;
  }

  .form-card {
    margin-top: 20px;
  }

  .editor-tip {
    margin-top: 8px;
  }
}
</style>
