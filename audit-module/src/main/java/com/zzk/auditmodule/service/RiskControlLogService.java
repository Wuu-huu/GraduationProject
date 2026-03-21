package com.zzk.auditmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.auditmodule.entity.RiskControlLog;
import com.zzk.auditmodule.vo.RiskControlLogVO;
import com.zzk.common.model.page.PageResponse;
import com.zzk.messagemodule.dto.PageQuery;

/**
* @author 周振坤
* @description 针对表【risk_control_log(风控日志表)】的数据库操作Service
* @createDate 2026-03-19 23:38:16
*/
public interface RiskControlLogService extends IService<RiskControlLog> {

    void createLog(Long uid, String riskType, Integer bizType, Long bizId, String detail, Integer riskLevel);

    PageResponse<RiskControlLogVO> listLogs(PageQuery query);
}
