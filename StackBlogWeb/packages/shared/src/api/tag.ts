import { http } from '../utils/request'
import { mockApi } from '../utils/mock'
import type { Tag } from '../types'

// 判断是否启用Mock
const isMockEnabled = import.meta.env.VITE_MOCK_ENABLED === 'true'

/**
 * 标签相关API
 */
export const tagApi = {
  /**
   * 获取标签列表
   */
  getList(): Promise<Tag[]> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 获取标签列表')
      return mockApi.getTagList()
    }
    return http.get<Tag[]>('/tags')
  },

  /**
   * 根据ID获取标签
   */
  getById(id: number): Promise<Tag> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 获取标签详情', id)
      return mockApi.getTagById(id)
    }
    return http.get<Tag>(`/tags/${id}`)
  },

  /**
   * 创建标签
   */
  create(data: { name: string; description?: string }): Promise<Tag> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 创建标签', data)
      return mockApi.createTag(data)
    }
    return http.post<Tag>('/tags', data)
  },

  /**
   * 更新标签
   */
  update(id: number, data: Partial<Tag>): Promise<Tag> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 更新标签', id, data)
      return mockApi.updateTag(id, data)
    }
    return http.put<Tag>(`/tags/${id}`, data)
  },

  /**
   * 删除标签
   */
  delete(id: number): Promise<void> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 删除标签', id)
      return mockApi.deleteTag(id)
    }
    return http.delete<void>(`/tags/${id}`)
  }
}
