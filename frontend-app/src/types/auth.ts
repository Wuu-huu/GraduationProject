export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  email?: string
  phone?: string
}

export interface CurrentUser {
  uid: number
  username: string
  role: number
  state: number
}

export interface LoginResult {
  accessToken: string
  expiresIn: number
  uid: number
  username: string
  role: number
}
