package com.zzk.messagemodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.messagemodule.dto.SendMessageRequest;
import com.zzk.messagemodule.entity.Conversation;
import com.zzk.messagemodule.entity.ConversationRead;
import com.zzk.messagemodule.entity.Message;
import com.zzk.messagemodule.mapper.ConversationMapper;
import com.zzk.messagemodule.mapper.ConversationReadMapper;
import com.zzk.messagemodule.mapper.MessageMapper;
import com.zzk.messagemodule.service.MessageService;
import com.zzk.messagemodule.vo.MessageVO;
import com.zzk.usermodule.entity.UserProfile;
import com.zzk.usermodule.service.UserProfileService;
import java.util.Date;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2026-03-19 23:36:46
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService {

    private final ConversationMapper conversationMapper;
    private final ConversationReadMapper conversationReadMapper;
    private final UserProfileService userProfileService;

    public MessageServiceImpl(ConversationMapper conversationMapper,
                              ConversationReadMapper conversationReadMapper,
                              UserProfileService userProfileService) {
        this.conversationMapper = conversationMapper;
        this.conversationReadMapper = conversationReadMapper;
        this.userProfileService = userProfileService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageVO sendMessage(Long conversationId, SendMessageRequest request) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        Conversation conversation = conversationMapper.selectById(conversationId);
        if (conversation == null || value(conversation.getStatus()) != 1) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Conversation not found");
        }
        if (!Objects.equals(conversation.getUserA(), currentUserId) && !Objects.equals(conversation.getUserB(), currentUserId)) {
            throw new BusinessException(ApiCodeEnum.FORBIDDEN, "You can only send messages in your own conversation");
        }
        if (!Objects.equals(request.getReceiverUid(), conversation.getUserA())
                && !Objects.equals(request.getReceiverUid(), conversation.getUserB())) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Receiver is not in this conversation");
        }
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setSenderUid(currentUserId);
        message.setReceiverUid(request.getReceiverUid());
        message.setMsgType(request.getMsgType());
        message.setContent(request.getContent().trim());
        message.setIsRecalled(0);
        message.setCreateTime(new Date());
        save(message);
        conversation.setLatestMessageId(message.getMessageId());
        conversation.setLatestTime(message.getCreateTime());
        conversationMapper.updateById(conversation);

        ConversationRead senderRead = getOrCreateRead(conversationId, currentUserId);
        senderRead.setLastReadMessageId(message.getMessageId());
        senderRead.setUnreadCount(0);
        senderRead.setUpdateTime(new Date());
        conversationReadMapper.updateById(senderRead);

        ConversationRead receiverRead = getOrCreateRead(conversationId, request.getReceiverUid());
        receiverRead.setUnreadCount(value(receiverRead.getUnreadCount()) + 1);
        receiverRead.setUpdateTime(new Date());
        conversationReadMapper.updateById(receiverRead);

        UserProfile senderProfile = userProfileService.getById(currentUserId);
        return MessageVO.builder()
                .messageId(message.getMessageId())
                .conversationId(conversationId)
                .senderUid(currentUserId)
                .receiverUid(request.getReceiverUid())
                .senderName(senderProfile == null ? null : senderProfile.getNickname())
                .senderAvatarUrl(senderProfile == null ? null : senderProfile.getAvatarUrl())
                .msgType(message.getMsgType())
                .content(message.getContent())
                .isRecalled(message.getIsRecalled())
                .createTime(message.getCreateTime())
                .build();
    }

    private ConversationRead getOrCreateRead(Long conversationId, Long uid) {
        ConversationRead read = conversationReadMapper.selectOne(Wrappers.<ConversationRead>lambdaQuery()
                .eq(ConversationRead::getConversationId, conversationId)
                .eq(ConversationRead::getUid, uid)
                .last("limit 1"));
        if (read != null) {
            return read;
        }
        read = new ConversationRead();
        read.setConversationId(conversationId);
        read.setUid(uid);
        read.setUnreadCount(0);
        read.setUpdateTime(new Date());
        conversationReadMapper.insert(read);
        return read;
    }

    private int value(Integer value) {
        return value == null ? 0 : value;
    }
}




