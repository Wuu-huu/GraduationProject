import type { AppRole } from '@/types/route'

export function hasRole(userRole: number | null | undefined, roles?: AppRole[]): boolean {
  if (!roles || roles.length === 0) {
    return true
  }

  const currentRole: AppRole =
    userRole === 1 ? 'admin' : userRole === 2 ? 'creator' : userRole ? 'user' : 'guest'

  return roles.includes(currentRole)
}
