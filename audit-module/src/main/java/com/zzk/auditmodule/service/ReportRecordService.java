package com.zzk.auditmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.auditmodule.dto.HandleReportRequest;
import com.zzk.auditmodule.entity.ReportRecord;
import com.zzk.auditmodule.vo.ReportRecordVO;
import com.zzk.common.model.page.PageResponse;
import com.zzk.messagemodule.dto.PageQuery;

/**
* @author 周振坤
* @description 针对表【report_record(举报表)】的数据库操作Service
* @createDate 2026-03-19 23:38:07
*/
public interface ReportRecordService extends IService<ReportRecord> {

    PageResponse<ReportRecordVO> listReports(PageQuery query);

    ReportRecordVO handleReport(Long reportId, HandleReportRequest request);
}
