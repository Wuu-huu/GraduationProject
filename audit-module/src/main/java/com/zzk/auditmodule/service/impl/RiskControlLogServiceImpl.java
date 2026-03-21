package com.zzk.auditmodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.auditmodule.entity.RiskControlLog;
import com.zzk.auditmodule.mapper.RiskControlLogMapper;
import com.zzk.auditmodule.service.RiskControlLogService;
import com.zzk.auditmodule.vo.RiskControlLogVO;
import com.zzk.common.model.page.PageResponse;
import com.zzk.messagemodule.dto.PageQuery;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【risk_control_log(风控日志表)】的数据库操作Service实现
* @createDate 2026-03-19 23:38:16
*/
@Service
public class RiskControlLogServiceImpl extends ServiceImpl<RiskControlLogMapper, RiskControlLog>
    implements RiskControlLogService{

    @Override
    public void createLog(Long uid, String riskType, Integer bizType, Long bizId, String detail, Integer riskLevel) {
        RiskControlLog log = new RiskControlLog();
        log.setUid(uid);
        log.setRiskType(riskType);
        log.setBizType(bizType);
        log.setBizId(bizId);
        log.setDetail(detail);
        log.setRiskLevel(riskLevel);
        log.setCreateTime(new Date());
        save(log);
    }

    @Override
    public PageResponse<RiskControlLogVO> listLogs(PageQuery query) {
        Page<RiskControlLog> page = page(new Page<>(query.getPageNum(), query.getPageSize()),
                Wrappers.<RiskControlLog>lambdaQuery()
                        .orderByDesc(RiskControlLog::getCreateTime)
                        .orderByDesc(RiskControlLog::getId));
        return PageResponse.<RiskControlLogVO>builder()
                .records(page.getRecords().stream().map(this::toVO).toList())
                .total(page.getTotal())
                .pageNum(page.getCurrent())
                .pageSize(page.getSize())
                .build();
    }

    private RiskControlLogVO toVO(RiskControlLog log) {
        return RiskControlLogVO.builder()
                .id(log.getId())
                .uid(log.getUid())
                .riskType(log.getRiskType())
                .bizType(log.getBizType())
                .bizId(log.getBizId())
                .detail(log.getDetail())
                .riskLevel(log.getRiskLevel())
                .createTime(log.getCreateTime())
                .build();
    }
}




