<template>
  <n-layout-header class="blog-header" bordered>
    <div class="header-container">
      <div class="logo" @click="router.push('/')">
        <span class="logo-icon">📝</span>
        <span class="logo-text">StackBlog</span>
      </div>

      <n-menu
        v-model:value="activeKey"
        mode="horizontal"
        :options="menuOptions"
        @update:value="handleMenuSelect"
      />

      <div class="header-actions">
        <n-input
          v-model:value="searchKeyword"
          placeholder="搜索文章..."
          clearable
          @keyup.enter="handleSearch"
        >
          <template #suffix>
            <n-icon :component="SearchOutline" @click="handleSearch" style="cursor: pointer" />
          </template>
        </n-input>
      </div>
    </div>
  </n-layout-header>
</template>

<script setup lang="ts">
import { ref, h, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { NIcon } from 'naive-ui'
import { 
  HomeOutline, 
  ArchiveOutline, 
  FolderOutline, 
  PricetagOutline, 
  InformationCircleOutline,
  SearchOutline 
} from '@vicons/ionicons5'

const router = useRouter()
const route = useRoute()
const searchKeyword = ref('')

const activeKey = computed(() => {
  const path = route.path
  if (path === '/') return 'home'
  if (path.startsWith('/archive')) return 'archive'
  if (path.startsWith('/category')) return 'category'
  if (path.startsWith('/tag')) return 'tag'
  if (path.startsWith('/about')) return 'about'
  return 'home'
})

const renderIcon = (icon: any) => {
  return () => h(NIcon, null, { default: () => h(icon) })
}

const menuOptions = [
  {
    label: '首页',
    key: 'home',
    icon: renderIcon(HomeOutline)
  },
  {
    label: '归档',
    key: 'archive',
    icon: renderIcon(ArchiveOutline)
  },
  {
    label: '分类',
    key: 'category',
    icon: renderIcon(FolderOutline)
  },
  {
    label: '标签',
    key: 'tag',
    icon: renderIcon(PricetagOutline)
  },
  {
    label: '关于',
    key: 'about',
    icon: renderIcon(InformationCircleOutline)
  }
]

const handleMenuSelect = (key: string) => {
  const path = key === 'home' ? '/' : `/${key}`
  // 使用 nextTick 确保 DOM 更新完成后再导航
  router.push(path).catch(err => {
    // 忽略导航重复错误
    if (err.name !== 'NavigationDuplicated') {
      console.error('Navigation error:', err)
    }
  })
}

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ 
      path: '/', 
      query: { keyword: searchKeyword.value.trim() } 
    }).catch(err => {
      // 忽略导航重复错误
      if (err.name !== 'NavigationDuplicated') {
        console.error('Navigation error:', err)
      }
    })
  }
}
</script>

<style scoped lang="scss">
.blog-header {
  position: sticky;
  top: 0;
  z-index: 1000;
  background: #fff;

  .header-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 20px;
  }

  .logo {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    user-select: none;
    flex-shrink: 0;

    .logo-icon {
      font-size: 28px;
    }

    .logo-text {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
    }

    &:hover .logo-text {
      color: #18a058;
    }
  }

  .header-actions {
    flex-shrink: 0;
    width: 200px;
  }
}

@media (max-width: 768px) {
  .header-actions {
    width: 150px;
  }
}
</style>
