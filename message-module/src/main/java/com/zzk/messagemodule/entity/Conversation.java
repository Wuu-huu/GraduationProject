package com.zzk.messagemodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName conversation
 */
@TableName(value ="conversation")
@Data
public class Conversation {
    private Long conversationId;

    private Integer conversationType;

    private Long userA;

    private Long userB;

    private Long latestMessageId;

    private Date latestTime;

    private Integer status;
}