export interface AuditItem {
  auditId: number
  bizType?: number
  bizId: number
  auditStatus: number
  reason?: string | null
  auditorUid?: number | null
  submitTime?: string
  auditTime?: string | null
}

export interface ReportItem {
  reportId: number
  reporterUid: number
  targetType: number
  targetId: number
  reasonType: number
  reasonText?: string | null
  status: number
  createTime?: string
  handleTime?: string | null
}
