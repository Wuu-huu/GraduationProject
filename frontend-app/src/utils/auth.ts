import { clearToken, getToken, setToken } from './storage'

export function readAccessToken(): string {
  return getToken()
}

export function writeAccessToken(token: string): void {
  setToken(token)
}

export function removeAccessToken(): void {
  clearToken()
}
