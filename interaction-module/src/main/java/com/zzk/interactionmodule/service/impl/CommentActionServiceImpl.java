package com.zzk.interactionmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.interactionmodule.entity.CommentAction;
import com.zzk.interactionmodule.service.CommentActionService;
import com.zzk.interactionmodule.mapper.CommentActionMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【comment_action(评论行为表)】的数据库操作Service实现
* @createDate 2026-03-19 23:34:43
*/
@Service
public class CommentActionServiceImpl extends ServiceImpl<CommentActionMapper, CommentAction>
    implements CommentActionService{

}




