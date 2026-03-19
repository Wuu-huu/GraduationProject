package com.zzk.messagemodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.messagemodule.entity.ConversationRead;
import com.zzk.messagemodule.service.ConversationReadService;
import com.zzk.messagemodule.mapper.ConversationReadMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【conversation_read(会话已读状态表)】的数据库操作Service实现
* @createDate 2026-03-19 23:36:31
*/
@Service
public class ConversationReadServiceImpl extends ServiceImpl<ConversationReadMapper, ConversationRead>
    implements ConversationReadService{

}




