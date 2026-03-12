import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

/**
 * 日期格式化工具
 */

/**
 * 格式化日期
 */
export function formatDate(date: string | Date, format: string = 'YYYY-MM-DD HH:mm:ss'): string {
  return dayjs(date).format(format)
}

/**
 * 相对时间（多久之前）
 */
export function fromNow(date: string | Date): string {
  return dayjs(date).fromNow()
}

/**
 * 智能时间显示
 * - 1分钟内: 刚刚
 * - 1小时内: X分钟前
 * - 今天: 今天 HH:mm
 * - 昨天: 昨天 HH:mm
 * - 今年: MM-DD HH:mm
 * - 更早: YYYY-MM-DD
 */
export function smartTime(date: string | Date): string {
  const now = dayjs()
  const target = dayjs(date)
  const diff = now.diff(target, 'minute')

  if (diff < 1) {
    return '刚刚'
  } else if (diff < 60) {
    return `${diff}分钟前`
  } else if (target.isSame(now, 'day')) {
    return '今天 ' + target.format('HH:mm')
  } else if (target.isSame(now.subtract(1, 'day'), 'day')) {
    return '昨天 ' + target.format('HH:mm')
  } else if (target.isSame(now, 'year')) {
    return target.format('MM-DD HH:mm')
  } else {
    return target.format('YYYY-MM-DD')
  }
}

/**
 * 是否是今天
 */
export function isToday(date: string | Date): boolean {
  return dayjs(date).isSame(dayjs(), 'day')
}

/**
 * 是否是本周
 */
export function isThisWeek(date: string | Date): boolean {
  return dayjs(date).isSame(dayjs(), 'week')
}

/**
 * 是否是本月
 */
export function isThisMonth(date: string | Date): boolean {
  return dayjs(date).isSame(dayjs(), 'month')
}

/**
 * 是否是本年
 */
export function isThisYear(date: string | Date): boolean {
  return dayjs(date).isSame(dayjs(), 'year')
}

/**
 * 获取年月日
 */
export function getYearMonth(date: string | Date): string {
  return dayjs(date).format('YYYY年MM月')
}

/**
 * 获取年份
 */
export function getYear(date: string | Date): number {
  return dayjs(date).year()
}

/**
 * 获取月份
 */
export function getMonth(date: string | Date): number {
  return dayjs(date).month() + 1
}
