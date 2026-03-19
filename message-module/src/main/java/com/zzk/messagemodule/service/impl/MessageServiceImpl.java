package com.zzk.messagemodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.messagemodule.entity.Message;
import com.zzk.messagemodule.service.MessageService;
import com.zzk.messagemodule.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2026-03-19 23:36:46
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}




