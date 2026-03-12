import { http } from '../utils/request'
import { mockApi } from '../utils/mock'
import type { LoginDTO, RegisterDTO, LoginResponse, UserResponseDTO } from '../types'

// 判断是否启用Mock
const isMockEnabled = import.meta.env.VITE_MOCK_ENABLED === 'true'

/**
 * 认证相关API
 */
export const authApi = {
  /**
   * 用户登录
   */
  login(data: LoginDTO): Promise<LoginResponse> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 登录请求')
      return mockApi.login(data.username, data.password)
    }
    return http.post<LoginResponse>('/auth/login', data)
  },

  /**
   * 用户注册
   */
  register(data: RegisterDTO): Promise<UserResponseDTO> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 注册请求')
      throw new Error('Mock模式暂不支持注册')
    }
    return http.post<UserResponseDTO>('/auth/register', data)
  },

  /**
   * 获取当前用户信息
   */
  getCurrentUser(): Promise<UserResponseDTO> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 获取用户信息')
      return mockApi.getCurrentUser()
    }
    return http.get<UserResponseDTO>('/auth/me')
  },

  /**
   * 退出登录（前端清除token）
   */
  logout(): void {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }
}
