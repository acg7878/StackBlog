import { http } from '../utils/request'
import { mockApi } from '../utils/mock'
import type { ArticleType } from '../types'

// 判断是否启用Mock
const isMockEnabled = import.meta.env.VITE_MOCK_ENABLED === 'true'

/**
 * 分类相关API
 */
export const categoryApi = {
  /**
   * 获取分类列表
   */
  getList(): Promise<ArticleType[]> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 获取分类列表')
      return mockApi.getCategoryList()
    }
    return http.get<ArticleType[]>('/types')
  },

  /**
   * 根据ID获取分类
   */
  getById(id: number): Promise<ArticleType> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 获取分类详情', id)
      return mockApi.getCategoryById(id)
    }
    return http.get<ArticleType>(`/types/${id}`)
  },

  /**
   * 创建分类
   */
  create(data: { name: string; description?: string; sort?: number; available?: boolean }): Promise<ArticleType> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 创建分类', data)
      return mockApi.createCategory(data)
    }
    return http.post<ArticleType>('/types', data)
  },

  /**
   * 更新分类
   */
  update(id: number, data: Partial<ArticleType>): Promise<ArticleType> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 更新分类', id, data)
      return mockApi.updateCategory(id, data)
    }
    return http.put<ArticleType>(`/types/${id}`, data)
  },

  /**
   * 删除分类
   */
  delete(id: number): Promise<void> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 删除分类', id)
      return mockApi.deleteCategory(id)
    }
    return http.delete<void>(`/types/${id}`)
  }
}
