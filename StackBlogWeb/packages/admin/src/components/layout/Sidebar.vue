<template>
  <div class="sidebar-container">
    <!-- Logo -->
    <div class="logo-container">
      <h1 v-if="!isCollapse" class="logo-title">StackBlog</h1>
      <h1 v-else class="logo-title-mini">S</h1>
    </div>

    <!-- 菜单 -->
    <el-menu
      :default-active="activeMenu"
      :collapse="isCollapse"
      :unique-opened="true"
      background-color="#001529"
      text-color="#fff"
      active-text-color="#409eff"
      router
    >
      <template v-for="route in menuRoutes" :key="route.path">
        <!-- 有子菜单 -->
        <el-sub-menu v-if="route.children && route.children.length > 0" :index="route.path">
          <template #title>
            <el-icon v-if="route.meta?.icon">
              <component :is="route.meta.icon" />
            </el-icon>
            <span>{{ route.meta?.title }}</span>
          </template>
          <el-menu-item
            v-for="child in route.children"
            :key="child.path"
            :index="child.path"
          >
            {{ child.meta?.title }}
          </el-menu-item>
        </el-sub-menu>

        <!-- 无子菜单 -->
        <el-menu-item v-else :index="route.path">
          <el-icon v-if="route.meta?.icon">
            <component :is="route.meta.icon" />
          </el-icon>
          <template #title>{{ route.meta?.title }}</template>
        </el-menu-item>
      </template>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

defineProps<{
  isCollapse: boolean
}>()

const route = useRoute()
const router = useRouter()

// 当前激活的菜单
const activeMenu = computed(() => {
  const { path } = route
  // 如果是编辑页，高亮列表页
  if (path.includes('/edit/')) {
    return path.substring(0, path.lastIndexOf('/edit'))
  }
  return path
})

// 菜单路由（过滤掉不需要显示的路由）
const menuRoutes = computed(() => {
  const routes = router.getRoutes()
  const mainRoute = routes.find(r => r.path === '/')
  
  if (!mainRoute?.children) return []
  
  return mainRoute.children
    .filter(route => {
      // 过滤掉没有title的路由
      return route.meta?.title && !route.meta?.hidden
    })
    .map(route => {
      // 处理子路由，构建完整路径
      if (route.children && route.children.length > 0) {
        return {
          ...route,
          children: route.children
            .filter(child => child.meta?.title && !child.meta?.hidden)
            .map(child => ({
              ...child,
              // 如果子路由的path不是以/开头，则拼接父路由path
              path: child.path.startsWith('/') ? child.path : `/${route.path}/${child.path}`.replace(/\/+/g, '/')
            }))
        }
      }
      return route
    })
})
</script>

<style scoped lang="scss">
.sidebar-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #002140;
  color: #fff;
  font-weight: bold;
  font-size: 20px;
  transition: all 0.3s;

  .logo-title {
    font-size: 20px;
  }

  .logo-title-mini {
    font-size: 24px;
  }
}

.el-menu {
  border-right: none;
  flex: 1;
  overflow-y: auto;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background: rgba(255, 255, 255, 0.2);
    border-radius: 3px;
  }
}
</style>
