import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@shared/api'
import { storage, token as tokenStorage } from '@shared/utils'
import type { UserResponseDTO, LoginDTO } from '@shared/types'

export const useUserStore = defineStore('user', () => {
  // 状态
  const userInfo = ref<UserResponseDTO | null>(null)
  const token = ref<string | null>(tokenStorage.get())

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const username = computed(() => userInfo.value?.username || '')
  const nickname = computed(() => userInfo.value?.nickname || '')
  const isAdmin = computed(() => {
    return userInfo.value?.roles?.includes('ROLE_ADMIN') || false
  })

  // 登录
  async function login(loginData: LoginDTO) {
    try {
      const response = await authApi.login(loginData)
      token.value = response.token
      userInfo.value = response.user
      
      // 保存到本地存储
      tokenStorage.set(response.token)
      storage.set('user', response.user)
      
      return response
    } catch (error) {
      console.error('登录失败:', error)
      throw error
    }
  }

  // 获取当前用户信息
  async function getCurrentUser() {
    try {
      const user = await authApi.getCurrentUser()
      userInfo.value = user
      storage.set('user', user)
      return user
    } catch (error) {
      console.error('获取用户信息失败:', error)
      // token可能过期，清除登录状态
      logout()
      throw error
    }
  }

  // 退出登录
  function logout() {
    token.value = null
    userInfo.value = null
    tokenStorage.remove()
    storage.remove('user')
    authApi.logout()
  }

  // 初始化用户信息（从本地存储恢复）
  function initUserInfo() {
    const savedUser = storage.get<UserResponseDTO>('user')
    const savedToken = tokenStorage.get()
    
    if (savedUser && savedToken) {
      userInfo.value = savedUser
      token.value = savedToken
    }
  }

  return {
    userInfo,
    token,
    isLoggedIn,
    username,
    nickname,
    isAdmin,
    login,
    getCurrentUser,
    logout,
    initUserInfo
  }
})
