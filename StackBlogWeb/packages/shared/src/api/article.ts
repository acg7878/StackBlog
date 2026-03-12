import { http } from '../utils/request'
import { mockApi } from '../utils/mock'
import type {
  Article,
  ArticleResponseDTO,
  ArticleDetailResponseDTO,
  ArticleDTO,
  ArticleQueryDTO,
  PageResponse
} from '../types'

// 判断是否启用Mock
const isMockEnabled = import.meta.env.VITE_MOCK_ENABLED === 'true'

/**
 * 文章相关API
 */
export const articleApi = {
  /**
   * 获取文章列表（分页）
   */
  getList(params: ArticleQueryDTO): Promise<PageResponse<ArticleResponseDTO>> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 获取文章列表', params)
      return mockApi.getArticleList(params)
    }
    return http.get<PageResponse<ArticleResponseDTO>>('/articles', { params })
  },

  /**
   * 获取文章详情
   */
  getDetail(id: number): Promise<ArticleDetailResponseDTO> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 获取文章详情', id)
      return mockApi.getArticleDetail(id)
    }
    return http.get<ArticleDetailResponseDTO>(`/articles/${id}`)
  },

  /**
   * 创建文章（需认证）
   */
  create(data: ArticleDTO): Promise<ArticleResponseDTO> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 创建文章', data)
      return mockApi.createArticle(data)
    }
    return http.post<ArticleResponseDTO>('/articles', data)
  },

  /**
   * 更新文章（需认证）
   */
  update(id: number, data: ArticleDTO): Promise<ArticleResponseDTO> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 更新文章', id, data)
      return mockApi.updateArticle(id, data)
    }
    return http.put<ArticleResponseDTO>(`/articles/${id}`, data)
  },

  /**
   * 删除文章（需认证）
   */
  delete(id: number): Promise<void> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 删除文章', id)
      return mockApi.deleteArticle(id)
    }
    return http.delete<void>(`/articles/${id}`)
  },

  /**
   * 获取推荐文章
   */
  getRecommended(size: number = 10): Promise<ArticleResponseDTO[]> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 获取推荐文章')
      return mockApi.getArticleList({ recommended: true, size }).then(res => res.content)
    }
    return http.get<ArticleResponseDTO[]>('/articles/recommended', { params: { size } })
  },

  /**
   * 获取最新文章
   */
  getRecent(size: number = 10): Promise<ArticleResponseDTO[]> {
    if (isMockEnabled) {
      console.log('🔧 [Mock模式] 获取最新文章')
      return mockApi.getArticleList({ size }).then(res => res.content)
    }
    return http.get<ArticleResponseDTO[]>('/articles/recent', { params: { size } })
  }
}
