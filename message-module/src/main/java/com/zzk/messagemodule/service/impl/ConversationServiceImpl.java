package com.zzk.messagemodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.messagemodule.entity.Conversation;
import com.zzk.messagemodule.service.ConversationService;
import com.zzk.messagemodule.mapper.ConversationMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【conversation(私信会话表)】的数据库操作Service实现
* @createDate 2026-03-19 23:36:06
*/
@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation>
    implements ConversationService{

}




