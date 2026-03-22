import request from './request'
import type { ApiResponse, PageResponse } from '@/types/api'
import type { AuditItem, ReportItem } from '@/types/admin'

export function getVideoAuditList(pageNum = 1, pageSize = 20): Promise<ApiResponse<PageResponse<AuditItem>>> {
  return request.get('/api/audit/videos', { params: { pageNum, pageSize } })
}

export function getCommentAuditList(pageNum = 1, pageSize = 20): Promise<ApiResponse<PageResponse<AuditItem>>> {
  return request.get('/api/audit/comments', { params: { pageNum, pageSize } })
}

export function getReportList(pageNum = 1, pageSize = 20): Promise<ApiResponse<PageResponse<ReportItem>>> {
  return request.get('/api/audit/reports', { params: { pageNum, pageSize } })
}

export function approveVideoAudit(auditId: number, reason = ''): Promise<ApiResponse<AuditItem>> {
  return request.post(`/api/audit/videos/${auditId}/approve`, { reason })
}

export function rejectVideoAudit(auditId: number, reason = ''): Promise<ApiResponse<AuditItem>> {
  return request.post(`/api/audit/videos/${auditId}/reject`, { reason })
}

export function approveCommentAudit(auditId: number, reason = ''): Promise<ApiResponse<AuditItem>> {
  return request.post(`/api/audit/comments/${auditId}/approve`, { reason })
}

export function rejectCommentAudit(auditId: number, reason = ''): Promise<ApiResponse<AuditItem>> {
  return request.post(`/api/audit/comments/${auditId}/reject`, { reason })
}

export function handleReport(reportId: number, status: number, reason = ''): Promise<ApiResponse<ReportItem>> {
  return request.post(`/api/audit/reports/${reportId}/handle`, { status, reason })
}
