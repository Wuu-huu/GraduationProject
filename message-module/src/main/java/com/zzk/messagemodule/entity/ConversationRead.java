package com.zzk.messagemodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName conversation_read
 */
@TableName(value ="conversation_read")
@Data
public class ConversationRead {
    private Long id;

    private Long conversationId;

    private Long uid;

    private Long lastReadMessageId;

    private Integer unreadCount;

    private Date updateTime;
}