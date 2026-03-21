package com.zzk.messagemodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.common.model.page.PageResponse;
import com.zzk.messagemodule.dto.CreateConversationRequest;
import com.zzk.messagemodule.dto.PageQuery;
import com.zzk.messagemodule.entity.Conversation;
import com.zzk.messagemodule.vo.ConversationVO;
import com.zzk.messagemodule.vo.MessageVO;

/**
* @author 周振坤
* @description 针对表【conversation(私信会话表)】的数据库操作Service
* @createDate 2026-03-19 23:36:06
*/
public interface ConversationService extends IService<Conversation> {

    ConversationVO createConversation(CreateConversationRequest request);

    PageResponse<ConversationVO> listConversations(PageQuery query);

    PageResponse<MessageVO> getConversationDetail(Long conversationId, PageQuery query);

    void markConversationRead(Long conversationId);
}
