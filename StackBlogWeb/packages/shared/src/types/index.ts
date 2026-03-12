// API响应通用类型
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 分页查询参数
export interface PageQuery {
  page?: number
  size?: number
  sortBy?: string
  sortDirection?: 'ASC' | 'DESC'
}

// 分页响应
export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
  first: boolean
  last: boolean
}

// 用户相关类型
export interface User {
  id: number
  username: string
  nickname: string
  email?: string
  avatar?: string
  createTime: string
  updateTime?: string
}

export interface UserResponseDTO extends User {
  roles?: string[]
}

export interface LoginDTO {
  username: string
  password: string
}

export interface RegisterDTO {
  username: string
  password: string
  nickname: string
  email?: string
}

export interface LoginResponse {
  token: string
  user: UserResponseDTO
}

// 文章相关类型
export interface Article {
  id: number
  userId: number
  title: string
  coverImage?: string
  typeId?: number
  top: boolean
  recommended: boolean
  original: boolean
  comment: boolean
  viewCount: number
  status: number // 0=草稿 1=已发布 2=已删除
  description?: string
  keywords?: string
  createTime: string
  updateTime?: string
}

export interface ArticleResponseDTO extends Article {
  author?: string
  typeName?: string
  commentCount?: number
  tags?: Tag[]
}

export interface ArticleDetailResponseDTO extends ArticleResponseDTO {
  content?: string
  contentMd?: string
}

export interface ArticleDTO {
  title: string
  content: string
  contentMd?: string
  description?: string
  coverImage?: string
  typeId?: number
  tags?: number[]
  top?: boolean
  recommended?: boolean
  original?: boolean
  comment?: boolean
  status?: number
}

export interface ArticleQueryDTO extends PageQuery {
  title?: string
  typeId?: number
  userId?: number
  status?: number
  recommended?: boolean
  top?: boolean
}

// 评论相关类型
export interface Comment {
  id: number
  userId?: number
  articleId: number
  parentId?: number
  nickname?: string
  email?: string
  qq?: string
  url?: string
  content: string
  ip?: string
  status: 'PENDING' | 'APPROVED' | 'REJECTED'
  support: number
  oppose: number
  createTime: string
}

export interface CommentResponseDTO extends Comment {
  replies?: CommentResponseDTO[]
}

export interface CommentDTO {
  articleId: number
  parentId?: number
  nickname?: string
  email?: string
  qq?: string
  url?: string
  content: string
}

// 分类相关类型
export interface ArticleType {
  id: number
  name: string
  description?: string
  sort: number
  available: boolean
  createTime: string
}

// 标签相关类型
export interface Tag {
  id: number
  name: string
  description?: string
  createTime: string
}

// 角色相关类型
export interface Role {
  id: number
  name: string
  description?: string
  createTime: string
}
