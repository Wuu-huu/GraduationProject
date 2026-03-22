const TOKEN_KEY = 'graduation-video-token'
const CREATOR_SERIES_KEY = 'graduation-video-creator-series-ids'

export function getToken(): string {
  return localStorage.getItem(TOKEN_KEY) ?? ''
}

export function setToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token)
}

export function clearToken(): void {
  localStorage.removeItem(TOKEN_KEY)
}

export function getCreatorSeriesIds(): number[] {
  const raw = localStorage.getItem(CREATOR_SERIES_KEY)
  if (!raw) return []

  try {
    const parsed = JSON.parse(raw) as number[]
    return Array.isArray(parsed) ? parsed : []
  } catch {
    return []
  }
}

export function pushCreatorSeriesId(seriesId: number): void {
  const current = getCreatorSeriesIds()
  const next = [seriesId, ...current.filter((item) => item !== seriesId)].slice(0, 20)
  localStorage.setItem(CREATOR_SERIES_KEY, JSON.stringify(next))
}

export function removeCreatorSeriesId(seriesId: number): void {
  const next = getCreatorSeriesIds().filter((item) => item !== seriesId)
  localStorage.setItem(CREATOR_SERIES_KEY, JSON.stringify(next))
}