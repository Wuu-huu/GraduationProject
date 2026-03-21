package com.zzk.auditmodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.auditmodule.dto.HandleReportRequest;
import com.zzk.auditmodule.entity.ReportRecord;
import com.zzk.auditmodule.mapper.ReportRecordMapper;
import com.zzk.auditmodule.service.ReportRecordService;
import com.zzk.auditmodule.service.RiskControlLogService;
import com.zzk.auditmodule.vo.ReportRecordVO;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.exception.ForbiddenException;
import com.zzk.common.model.page.PageResponse;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.messagemodule.dto.PageQuery;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【report_record(举报表)】的数据库操作Service实现
* @createDate 2026-03-19 23:38:07
*/
@Service
public class ReportRecordServiceImpl extends ServiceImpl<ReportRecordMapper, ReportRecord>
    implements ReportRecordService{

    private final RiskControlLogService riskControlLogService;

    public ReportRecordServiceImpl(RiskControlLogService riskControlLogService) {
        this.riskControlLogService = riskControlLogService;
    }

    @Override
    public PageResponse<ReportRecordVO> listReports(PageQuery query) {
        assertAdmin();
        Page<ReportRecord> page = page(new Page<>(query.getPageNum(), query.getPageSize()),
                Wrappers.<ReportRecord>lambdaQuery()
                        .orderByDesc(ReportRecord::getCreateTime)
                        .orderByDesc(ReportRecord::getReportId));
        return PageResponse.<ReportRecordVO>builder()
                .records(page.getRecords().stream().map(this::toVO).toList())
                .total(page.getTotal())
                .pageNum(page.getCurrent())
                .pageSize(page.getSize())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReportRecordVO handleReport(Long reportId, HandleReportRequest request) {
        assertAdmin();
        ReportRecord record = getById(reportId);
        if (record == null) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "举报记录不存在");
        }
        record.setStatus(request.getStatus());
        record.setHandleTime(new Date());
        updateById(record);
        riskControlLogService.createLog(record.getReporterUid(), "举报处理", record.getTargetType(),
                record.getTargetId(), request.getReason(), 1);
        return toVO(record);
    }

    private ReportRecordVO toVO(ReportRecord record) {
        return ReportRecordVO.builder()
                .reportId(record.getReportId())
                .reporterUid(record.getReporterUid())
                .targetType(record.getTargetType())
                .targetId(record.getTargetId())
                .reasonType(record.getReasonType())
                .reasonText(record.getReasonText())
                .status(record.getStatus())
                .createTime(record.getCreateTime())
                .handleTime(record.getHandleTime())
                .build();
    }

    private void assertAdmin() {
        if (!SecurityContextUtils.getLoginUser().isAdmin()) {
            throw new ForbiddenException("仅管理员可执行该操作");
        }
    }
}




