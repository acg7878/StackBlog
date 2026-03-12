import { http } from '../utils/request'
import { mockApi } from '../utils/mock'
import type { CommentResponseDTO, PageResponse } from '../types'

// 判断是否启用Mock
const isMockEnabled = import.meta.env.VITE_MOCK_ENABLED === 'true'

/**
 * 评论相关API
 */
export const commentApi = {
  /**
   * 获取评论列表（分页）
   */
  getList(params: {
    page?: number
    size?: number
    status?: string
    articleId?: number
  }): Promise<PageResponse<CommentResponseDTO>> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 获取评论列表', params)
      return mockApi.getCommentList(params)
    }
    return http.get<PageResponse<CommentResponseDTO>>('/comments', { params })
  },

  /**
   * 审核通过评论
   */
  approve(id: number): Promise<CommentResponseDTO> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 审核通过评论', id)
      return mockApi.approveComment(id)
    }
    return http.put<CommentResponseDTO>(`/comments/${id}/approve`)
  },

  /**
   * 拒绝评论
   */
  reject(id: number): Promise<CommentResponseDTO> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 拒绝评论', id)
      return mockApi.rejectComment(id)
    }
    return http.put<CommentResponseDTO>(`/comments/${id}/reject`)
  },

  /**
   * 删除评论
   */
  delete(id: number): Promise<void> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 删除评论', id)
      return mockApi.deleteComment(id)
    }
    return http.delete<void>(`/comments/${id}`)
  }
}
