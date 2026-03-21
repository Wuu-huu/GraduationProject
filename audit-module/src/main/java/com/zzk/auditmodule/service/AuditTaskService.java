package com.zzk.auditmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.auditmodule.dto.AuditDecisionRequest;
import com.zzk.auditmodule.dto.AuditPageQuery;
import com.zzk.auditmodule.entity.AuditTask;
import com.zzk.auditmodule.vo.AuditTaskVO;
import com.zzk.common.model.page.PageResponse;

/**
* @author 周振坤
* @description 针对表【audit_task(审核任务表)】的数据库操作Service
* @createDate 2026-03-19 23:37:42
*/
public interface AuditTaskService extends IService<AuditTask> {

    PageResponse<AuditTaskVO> listVideoAuditTasks(AuditPageQuery query);

    PageResponse<AuditTaskVO> listCommentAuditTasks(AuditPageQuery query);

    AuditTaskVO approveAuditTask(Long auditId, AuditDecisionRequest request);

    AuditTaskVO rejectAuditTask(Long auditId, AuditDecisionRequest request);
}
