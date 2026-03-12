import axios, { type AxiosRequestConfig, type AxiosResponse } from 'axios'
import type { ApiResponse } from '../types'

// 判断是否启用Mock模式
const isMockEnabled = import.meta.env.VITE_MOCK_ENABLED === 'true'

// 创建axios实例
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 从localStorage获取token（使用正确的键名）
    const token = localStorage.getItem('stackblog_token')
    
    // 🔍 调试日志
    console.log('=== 请求拦截器 ===')
    console.log('请求URL:', config.url)
    console.log('请求方法:', config.method?.toUpperCase())
    console.log('Token存在:', !!token)
    if (token) {
      // Token 以 JSON 字符串形式存储，需要解析
      const tokenValue = JSON.parse(token)
      console.log('Token前20字符:', tokenValue.substring(0, 20))
      config.headers.Authorization = `Bearer ${tokenValue}`
      console.log('✅ Authorization已添加')
    } else {
      console.warn('⚠️ 未找到Token，请先登录')
    }
    
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    // Mock模式：直接返回数据（Mock数据未包装在ResponseDTO中）
    if (isMockEnabled) {
      return response.data
    }

    // 真实接口：解包ResponseDTO
    const { code, data, message } = response.data

    // 业务成功
    if (code === 200) {
      return data
    }

    // 业务失败
    console.error('业务错误:', message)
    return Promise.reject(new Error(message || '请求失败'))
  },
  (error) => {
    // HTTP错误
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          // 未登录或token过期
          console.error('未授权，请重新登录')
          localStorage.removeItem('token')
          localStorage.removeItem('user')
          // 跳转到登录页（由具体项目处理）
          window.dispatchEvent(new CustomEvent('unauthorized'))
          break
        case 403:
          console.error('没有权限访问')
          break
        case 404:
          console.error('请求的资源不存在')
          break
        case 500:
          console.error('服务器错误')
          break
        default:
          console.error(`请求失败: ${data?.message || error.message}`)
      }
    } else if (error.request) {
      console.error('网络错误，请检查网络连接')
    } else {
      console.error('请求配置错误:', error.message)
    }
    
    return Promise.reject(error)
  }
)

// 封装请求方法
export const http = {
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return request.get(url, config)
  },

  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return request.post(url, data, config)
  },

  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return request.put(url, data, config)
  },

  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return request.delete(url, config)
  },

  patch<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return request.patch(url, data, config)
  }
}

export default request
