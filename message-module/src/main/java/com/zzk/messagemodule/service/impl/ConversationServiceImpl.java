package com.zzk.messagemodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.authmodule.entity.UserInfo;
import com.zzk.authmodule.service.UserInfoService;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.model.page.PageResponse;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.messagemodule.dto.CreateConversationRequest;
import com.zzk.messagemodule.dto.PageQuery;
import com.zzk.messagemodule.entity.Conversation;
import com.zzk.messagemodule.entity.ConversationRead;
import com.zzk.messagemodule.entity.Message;
import com.zzk.messagemodule.enums.ConversationTypeEnum;
import com.zzk.messagemodule.mapper.ConversationMapper;
import com.zzk.messagemodule.mapper.ConversationReadMapper;
import com.zzk.messagemodule.mapper.MessageMapper;
import com.zzk.messagemodule.service.ConversationService;
import com.zzk.messagemodule.vo.ConversationVO;
import com.zzk.messagemodule.vo.MessageVO;
import com.zzk.usermodule.entity.UserProfile;
import com.zzk.usermodule.entity.UserSetting;
import com.zzk.usermodule.service.UserProfileService;
import com.zzk.usermodule.service.UserSettingService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【conversation(私信会话表)】的数据库操作Service实现
* @createDate 2026-03-19 23:36:06
*/
@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation>
        implements ConversationService {

    private final ConversationReadMapper conversationReadMapper;
    private final MessageMapper messageMapper;
    private final UserInfoService userInfoService;
    private final UserProfileService userProfileService;
    private final UserSettingService userSettingService;

    public ConversationServiceImpl(ConversationReadMapper conversationReadMapper,
                                   MessageMapper messageMapper,
                                   UserInfoService userInfoService,
                                   UserProfileService userProfileService,
                                   UserSettingService userSettingService) {
        this.conversationReadMapper = conversationReadMapper;
        this.messageMapper = messageMapper;
        this.userInfoService = userInfoService;
        this.userProfileService = userProfileService;
        this.userSettingService = userSettingService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConversationVO createConversation(CreateConversationRequest request) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        if (Objects.equals(currentUserId, request.getTargetUid())) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Cannot create conversation with yourself");
        }
        UserInfo target = userInfoService.getById(request.getTargetUid());
        if (target == null) {
            throw new BusinessException(ApiCodeEnum.ACCOUNT_NOT_FOUND);
        }
        UserSetting userSetting = userSettingService.getById(request.getTargetUid());
        if (userSetting != null && value(userSetting.getOpenDm()) == 0) {
            throw new BusinessException(ApiCodeEnum.FORBIDDEN, "The target user has disabled direct messages");
        }
        Conversation conversation = findConversation(currentUserId, request.getTargetUid());
        if (conversation == null) {
            conversation = new Conversation();
            conversation.setConversationType(ConversationTypeEnum.PRIVATE.getCode());
            conversation.setUserA(Math.min(currentUserId, request.getTargetUid()));
            conversation.setUserB(Math.max(currentUserId, request.getTargetUid()));
            conversation.setStatus(1);
            save(conversation);
            initConversationRead(conversation.getConversationId(), currentUserId);
            initConversationRead(conversation.getConversationId(), request.getTargetUid());
        }
        return toConversationVO(conversation, currentUserId);
    }

    @Override
    public PageResponse<ConversationVO> listConversations(PageQuery query) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        Page<Conversation> page = page(new Page<>(query.getPageNum(), query.getPageSize()),
                Wrappers.<Conversation>lambdaQuery()
                        .and(wrapper -> wrapper.eq(Conversation::getUserA, currentUserId)
                                .or()
                                .eq(Conversation::getUserB, currentUserId))
                        .eq(Conversation::getStatus, 1)
                        .orderByDesc(Conversation::getLatestTime)
                        .orderByDesc(Conversation::getConversationId));
        return PageResponse.<ConversationVO>builder()
                .records(page.getRecords().stream().map(item -> toConversationVO(item, currentUserId)).toList())
                .total(page.getTotal())
                .pageNum(page.getCurrent())
                .pageSize(page.getSize())
                .build();
    }

    @Override
    public PageResponse<MessageVO> getConversationDetail(Long conversationId, PageQuery query) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        Conversation conversation = requireParticipant(conversationId, currentUserId);
        Page<Message> page = messageMapper.selectPage(new Page<>(query.getPageNum(), query.getPageSize()),
                Wrappers.<Message>lambdaQuery()
                        .eq(Message::getConversationId, conversation.getConversationId())
                        .orderByDesc(Message::getMessageId));
        return PageResponse.<MessageVO>builder()
                .records(toMessageVOs(page.getRecords()))
                .total(page.getTotal())
                .pageNum(page.getCurrent())
                .pageSize(page.getSize())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markConversationRead(Long conversationId) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        requireParticipant(conversationId, currentUserId);
        ConversationRead read = getConversationRead(conversationId, currentUserId);
        Conversation conversation = getById(conversationId);
        read.setLastReadMessageId(conversation == null ? null : conversation.getLatestMessageId());
        read.setUnreadCount(0);
        read.setUpdateTime(new Date());
        conversationReadMapper.updateById(read);
    }

    private Conversation findConversation(Long uidA, Long uidB) {
        return getOne(Wrappers.<Conversation>lambdaQuery()
                .eq(Conversation::getUserA, Math.min(uidA, uidB))
                .eq(Conversation::getUserB, Math.max(uidA, uidB))
                .last("limit 1"));
    }

    private Conversation requireParticipant(Long conversationId, Long uid) {
        Conversation conversation = getById(conversationId);
        if (conversation == null || value(conversation.getStatus()) != 1) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Conversation not found");
        }
        if (!Objects.equals(conversation.getUserA(), uid) && !Objects.equals(conversation.getUserB(), uid)) {
            throw new BusinessException(ApiCodeEnum.FORBIDDEN, "You can only access your own conversation");
        }
        return conversation;
    }

    private ConversationVO toConversationVO(Conversation conversation, Long currentUserId) {
        Long targetUid = Objects.equals(conversation.getUserA(), currentUserId) ? conversation.getUserB() : conversation.getUserA();
        UserInfo userInfo = userInfoService.getById(targetUid);
        UserProfile userProfile = userProfileService.getById(targetUid);
        Message latestMessage = conversation.getLatestMessageId() == null ? null : messageMapper.selectById(conversation.getLatestMessageId());
        ConversationRead read = getConversationRead(conversation.getConversationId(), currentUserId);
        return ConversationVO.builder()
                .conversationId(conversation.getConversationId())
                .conversationType(conversation.getConversationType())
                .targetUid(targetUid)
                .targetName(userProfile != null && userProfile.getNickname() != null ? userProfile.getNickname()
                        : userInfo == null ? null : userInfo.getUsername())
                .targetAvatarUrl(userProfile == null ? null : userProfile.getAvatarUrl())
                .latestMessageId(conversation.getLatestMessageId())
                .latestContent(latestMessage == null ? null : latestMessage.getContent())
                .latestTime(conversation.getLatestTime())
                .unreadCount(read == null ? 0 : value(read.getUnreadCount()))
                .build();
    }

    private List<MessageVO> toMessageVOs(List<Message> messages) {
        if (messages.isEmpty()) {
            return List.of();
        }
        List<Long> senderIds = messages.stream().map(Message::getSenderUid).distinct().toList();
        Map<Long, UserInfo> userInfoMap = senderIds.stream()
                .map(userInfoService::getById)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserInfo::getUid, Function.identity()));
        Map<Long, UserProfile> profileMap = senderIds.stream()
                .map(userProfileService::getById)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserProfile::getUid, Function.identity()));
        return messages.stream().map(message -> {
            UserInfo userInfo = userInfoMap.get(message.getSenderUid());
            UserProfile profile = profileMap.get(message.getSenderUid());
            return MessageVO.builder()
                    .messageId(message.getMessageId())
                    .conversationId(message.getConversationId())
                    .senderUid(message.getSenderUid())
                    .receiverUid(message.getReceiverUid())
                    .senderName(profile != null && profile.getNickname() != null ? profile.getNickname()
                            : userInfo == null ? null : userInfo.getUsername())
                    .senderAvatarUrl(profile == null ? null : profile.getAvatarUrl())
                    .msgType(message.getMsgType())
                    .content(message.getContent())
                    .isRecalled(message.getIsRecalled())
                    .createTime(message.getCreateTime())
                    .build();
        }).toList();
    }

    private void initConversationRead(Long conversationId, Long uid) {
        ConversationRead read = new ConversationRead();
        read.setConversationId(conversationId);
        read.setUid(uid);
        read.setUnreadCount(0);
        read.setUpdateTime(new Date());
        conversationReadMapper.insert(read);
    }

    private ConversationRead getConversationRead(Long conversationId, Long uid) {
        ConversationRead read = conversationReadMapper.selectOne(Wrappers.<ConversationRead>lambdaQuery()
                .eq(ConversationRead::getConversationId, conversationId)
                .eq(ConversationRead::getUid, uid)
                .last("limit 1"));
        if (read != null) {
            return read;
        }
        initConversationRead(conversationId, uid);
        return conversationReadMapper.selectOne(Wrappers.<ConversationRead>lambdaQuery()
                .eq(ConversationRead::getConversationId, conversationId)
                .eq(ConversationRead::getUid, uid)
                .last("limit 1"));
    }

    private int value(Integer value) {
        return value == null ? 0 : value;
    }
}




