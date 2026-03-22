export function formatCount(value?: number | null): string {
  if (!value) {
    return '0'
  }

  if (value >= 10000) {
    return `${(value / 10000).toFixed(1)}万`
  }

  return String(value)
}

export function formatDateTime(value?: string | null): string {
  if (!value) {
    return '--'
  }

  return value.replace('T', ' ').slice(0, 16)
}
