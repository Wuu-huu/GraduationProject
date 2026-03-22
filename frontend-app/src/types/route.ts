export type AppRole = 'guest' | 'user' | 'creator' | 'admin'

export interface RouteMetaConfig {
  title: string
  layout?: 'main' | 'creator' | 'admin' | 'blank'
  requiresAuth?: boolean
  roles?: AppRole[]
  hidden?: boolean
}
