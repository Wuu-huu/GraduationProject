package com.zzk.messagemodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.messagemodule.dto.SendMessageRequest;
import com.zzk.messagemodule.entity.Message;
import com.zzk.messagemodule.vo.MessageVO;

/**
* @author 周振坤
* @description 针对表【message(消息表)】的数据库操作Service
* @createDate 2026-03-19 23:36:46
*/
public interface MessageService extends IService<Message> {

    MessageVO sendMessage(Long conversationId, SendMessageRequest request);
}
