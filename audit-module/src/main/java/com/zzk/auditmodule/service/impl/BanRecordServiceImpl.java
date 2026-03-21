package com.zzk.auditmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.auditmodule.dto.BanUserRequest;
import com.zzk.auditmodule.entity.BanRecord;
import com.zzk.auditmodule.mapper.BanRecordMapper;
import com.zzk.auditmodule.enums.BanTypeEnum;
import com.zzk.auditmodule.service.BanRecordService;
import com.zzk.auditmodule.service.RiskControlLogService;
import com.zzk.auditmodule.vo.BanRecordVO;
import com.zzk.authmodule.entity.UserInfo;
import com.zzk.authmodule.service.UserInfoService;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.enums.UserStateEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.exception.ForbiddenException;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.messagemodule.facade.MessageNoticeFacade;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【ban_record(封禁记录表)】的数据库操作Service实现
* @createDate 2026-03-19 23:38:26
*/
@Service
public class BanRecordServiceImpl extends ServiceImpl<BanRecordMapper, BanRecord>
    implements BanRecordService{

    private final UserInfoService userInfoService;
    private final RiskControlLogService riskControlLogService;
    private final MessageNoticeFacade messageNoticeFacade;

    public BanRecordServiceImpl(UserInfoService userInfoService,
                                RiskControlLogService riskControlLogService,
                                MessageNoticeFacade messageNoticeFacade) {
        this.userInfoService = userInfoService;
        this.riskControlLogService = riskControlLogService;
        this.messageNoticeFacade = messageNoticeFacade;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BanRecordVO banUser(Long uid, BanUserRequest request) {
        assertAdmin();
        if (request.getBanType() == null) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "处罚类型不能为空");
        }
        UserInfo userInfo = userInfoService.getById(uid);
        if (userInfo == null) {
            throw new BusinessException(ApiCodeEnum.ACCOUNT_NOT_FOUND);
        }

        Date now = new Date();
        BanRecord record = new BanRecord();
        record.setUid(uid);
        record.setBanType(request.getBanType());
        record.setStartTime(now);
        record.setEndTime(request.getEndTime());
        record.setReason(blankToNull(request.getReason()));
        record.setOperatorUid(SecurityContextUtils.getCurrentUserId());
        record.setStatus(1);
        record.setCreateTime(now);
        save(record);

        if (request.getBanType() != null && request.getBanType() == BanTypeEnum.BAN.getCode()) {
            userInfo.setState(UserStateEnum.BANNED.getCode());
            userInfoService.updateById(userInfo);
        }

        String actionName = request.getBanType() != null && request.getBanType() == BanTypeEnum.MUTE.getCode()
                ? "用户禁言"
                : "用户封禁";
        riskControlLogService.createLog(uid, actionName, null, record.getId(), blankToNull(request.getReason()), 2);
        messageNoticeFacade.createAuditNotification(uid, request.getBanType(), record.getId(),
                actionName, buildNoticeContent(request));
        return toVO(record);
    }

    private BanRecordVO toVO(BanRecord record) {
        return BanRecordVO.builder()
                .id(record.getId())
                .uid(record.getUid())
                .banType(record.getBanType())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .reason(record.getReason())
                .operatorUid(record.getOperatorUid())
                .status(record.getStatus())
                .createTime(record.getCreateTime())
                .build();
    }

    private String buildNoticeContent(BanUserRequest request) {
        String actionName = request.getBanType() != null && request.getBanType() == BanTypeEnum.MUTE.getCode()
                ? "你已被禁言"
                : "你的账号已被封禁";
        return blankToEmpty(request.getReason()).isBlank()
                ? actionName
                : actionName + "，原因：" + request.getReason().trim();
    }

    private void assertAdmin() {
        if (!SecurityContextUtils.getLoginUser().isAdmin()) {
            throw new ForbiddenException("仅管理员可执行该操作");
        }
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String blankToEmpty(String value) {
        return value == null ? "" : value;
    }

}




