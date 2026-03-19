package com.zzk.interactionmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.interactionmodule.entity.Comment;
import com.zzk.interactionmodule.service.CommentService;
import com.zzk.interactionmodule.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2026-03-19 23:34:29
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




