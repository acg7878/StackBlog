/**
 * 本地存储工具类
 */

const STORAGE_PREFIX = 'stackblog_'

export const storage = {
  /**
   * 设置存储
   */
  set(key: string, value: any): void {
    const fullKey = STORAGE_PREFIX + key
    try {
      const stringValue = JSON.stringify(value)
      localStorage.setItem(fullKey, stringValue)
    } catch (error) {
      console.error('存储失败:', error)
    }
  },

  /**
   * 获取存储
   */
  get<T = any>(key: string): T | null {
    const fullKey = STORAGE_PREFIX + key
    try {
      const stringValue = localStorage.getItem(fullKey)
      if (stringValue === null) return null
      return JSON.parse(stringValue) as T
    } catch (error) {
      console.error('获取存储失败:', error)
      return null
    }
  },

  /**
   * 删除存储
   */
  remove(key: string): void {
    const fullKey = STORAGE_PREFIX + key
    localStorage.removeItem(fullKey)
  },

  /**
   * 清空所有存储
   */
  clear(): void {
    const keys = Object.keys(localStorage)
    keys.forEach((key) => {
      if (key.startsWith(STORAGE_PREFIX)) {
        localStorage.removeItem(key)
      }
    })
  },

  /**
   * 检查是否存在
   */
  has(key: string): boolean {
    const fullKey = STORAGE_PREFIX + key
    return localStorage.getItem(fullKey) !== null
  }
}

// Token相关
export const token = {
  set(token: string): void {
    storage.set('token', token)
  },

  get(): string | null {
    return storage.get<string>('token')
  },

  remove(): void {
    storage.remove('token')
  },

  has(): boolean {
    return storage.has('token')
  }
}

// 用户信息相关
export const user = {
  set(user: any): void {
    storage.set('user', user)
  },

  get<T = any>(): T | null {
    return storage.get<T>('user')
  },

  remove(): void {
    storage.remove('user')
  },

  has(): boolean {
    return storage.has('user')
  }
}
