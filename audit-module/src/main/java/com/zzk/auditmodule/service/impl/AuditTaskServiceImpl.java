package com.zzk.auditmodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.auditmodule.dto.AuditDecisionRequest;
import com.zzk.auditmodule.dto.AuditPageQuery;
import com.zzk.auditmodule.entity.AuditTask;
import com.zzk.auditmodule.mapper.AuditTaskMapper;
import com.zzk.auditmodule.enums.AuditBizTypeEnum;
import com.zzk.auditmodule.enums.AuditStatusEnum;
import com.zzk.auditmodule.service.AuditTaskService;
import com.zzk.auditmodule.service.RiskControlLogService;
import com.zzk.auditmodule.vo.AuditTaskVO;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.exception.ForbiddenException;
import com.zzk.common.model.page.PageResponse;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.interactionmodule.entity.Comment;
import com.zzk.interactionmodule.enums.CommentStatusEnum;
import com.zzk.interactionmodule.service.CommentService;
import com.zzk.messagemodule.facade.MessageNoticeFacade;
import com.zzk.videomodule.entity.Video;
import com.zzk.videomodule.enums.VideoPublishStatusEnum;
import com.zzk.videomodule.service.VideoService;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【audit_task(审核任务表)】的数据库操作Service实现
* @createDate 2026-03-19 23:37:42
*/
@Service
public class AuditTaskServiceImpl extends ServiceImpl<AuditTaskMapper, AuditTask>
    implements AuditTaskService{

    private final VideoService videoService;
    private final CommentService commentService;
    private final MessageNoticeFacade messageNoticeFacade;
    private final RiskControlLogService riskControlLogService;

    public AuditTaskServiceImpl(VideoService videoService,
                                CommentService commentService,
                                MessageNoticeFacade messageNoticeFacade,
                                RiskControlLogService riskControlLogService) {
        this.videoService = videoService;
        this.commentService = commentService;
        this.messageNoticeFacade = messageNoticeFacade;
        this.riskControlLogService = riskControlLogService;
    }

    @Override
    public PageResponse<AuditTaskVO> listVideoAuditTasks(AuditPageQuery query) {
        return listTasks(AuditBizTypeEnum.VIDEO.getCode(), query);
    }

    @Override
    public PageResponse<AuditTaskVO> listCommentAuditTasks(AuditPageQuery query) {
        return listTasks(AuditBizTypeEnum.COMMENT.getCode(), query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuditTaskVO approveAuditTask(Long auditId, AuditDecisionRequest request) {
        assertAdmin();
        AuditTask task = requirePendingTask(auditId);
        task.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        task.setReason(normalizeReason(request));
        task.setAuditorUid(SecurityContextUtils.getCurrentUserId());
        task.setAuditTime(new Date());
        updateById(task);
        applyDecision(task, true);
        return toVO(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuditTaskVO rejectAuditTask(Long auditId, AuditDecisionRequest request) {
        assertAdmin();
        AuditTask task = requirePendingTask(auditId);
        task.setAuditStatus(AuditStatusEnum.REJECTED.getCode());
        task.setReason(normalizeReason(request));
        task.setAuditorUid(SecurityContextUtils.getCurrentUserId());
        task.setAuditTime(new Date());
        updateById(task);
        applyDecision(task, false);
        return toVO(task);
    }

    private PageResponse<AuditTaskVO> listTasks(Integer bizType, AuditPageQuery query) {
        assertAdmin();
        Page<AuditTask> page = page(new Page<>(query.getPageNum(), query.getPageSize()),
                Wrappers.<AuditTask>lambdaQuery()
                        .eq(AuditTask::getBizType, bizType)
                        .eq(query.getAuditStatus() != null, AuditTask::getAuditStatus, query.getAuditStatus())
                        .orderByAsc(AuditTask::getAuditStatus)
                        .orderByDesc(AuditTask::getSubmitTime)
                        .orderByDesc(AuditTask::getAuditId));
        return PageResponse.<AuditTaskVO>builder()
                .records(page.getRecords().stream().map(this::toVO).toList())
                .total(page.getTotal())
                .pageNum(page.getCurrent())
                .pageSize(page.getSize())
                .build();
    }

    private AuditTask requirePendingTask(Long auditId) {
        AuditTask task = getById(auditId);
        if (task == null) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "审核任务不存在");
        }
        if (value(task.getAuditStatus()) != AuditStatusEnum.PENDING.getCode()) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "审核任务已处理");
        }
        return task;
    }

    private void applyDecision(AuditTask task, boolean approved) {
        if (value(task.getBizType()) == AuditBizTypeEnum.VIDEO.getCode()) {
            Video video = videoService.getById(task.getBizId());
            if (video == null) {
                throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "视频不存在");
            }
            video.setPublishStatus(approved
                    ? VideoPublishStatusEnum.PUBLISHED.getCode()
                    : VideoPublishStatusEnum.OFFLINE.getCode());
            if (approved && video.getPublishTime() == null) {
                video.setPublishTime(new Date());
            }
            videoService.updateById(video);
            messageNoticeFacade.createAuditNotification(video.getUid(), task.getBizType(), task.getBizId(),
                    approved ? "视频审核通过" : "视频审核驳回", buildAuditContent(task, approved));
            riskControlLogService.createLog(video.getUid(), approved ? "视频审核通过" : "视频审核驳回",
                    task.getBizType(), task.getBizId(), task.getReason(), approved ? 0 : 2);
            return;
        }

        if (value(task.getBizType()) == AuditBizTypeEnum.COMMENT.getCode()) {
            Comment comment = commentService.getById(task.getBizId());
            if (comment == null) {
                throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "评论不存在");
            }
            comment.setStatus(approved ? CommentStatusEnum.NORMAL.getCode() : CommentStatusEnum.DELETED.getCode());
            comment.setUpdateTime(new Date());
            commentService.updateById(comment);
            messageNoticeFacade.createAuditNotification(comment.getUid(), task.getBizType(), task.getBizId(),
                    approved ? "评论审核通过" : "评论审核驳回", buildAuditContent(task, approved));
            riskControlLogService.createLog(comment.getUid(), approved ? "评论审核通过" : "评论审核驳回",
                    task.getBizType(), task.getBizId(), task.getReason(), approved ? 0 : 2);
        }
    }

    private String buildAuditContent(AuditTask task, boolean approved) {
        String action = approved ? "已通过审核" : "未通过审核";
        return task.getReason() == null || task.getReason().isBlank()
                ? action
                : action + "，原因：" + task.getReason();
    }

    private String normalizeReason(AuditDecisionRequest request) {
        if (request == null || request.getReason() == null || request.getReason().isBlank()) {
            return null;
        }
        return request.getReason().trim();
    }

    private AuditTaskVO toVO(AuditTask task) {
        return AuditTaskVO.builder()
                .auditId(task.getAuditId())
                .bizType(task.getBizType())
                .bizId(task.getBizId())
                .auditStatus(task.getAuditStatus())
                .reason(task.getReason())
                .auditorUid(task.getAuditorUid())
                .submitTime(task.getSubmitTime())
                .auditTime(task.getAuditTime())
                .build();
    }

    private void assertAdmin() {
        if (!SecurityContextUtils.getLoginUser().isAdmin()) {
            throw new ForbiddenException("仅管理员可执行该操作");
        }
    }

    private int value(Integer value) {
        return value == null ? 0 : value;
    }
}




