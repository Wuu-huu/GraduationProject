package com.zzk.messagemodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName message
 */
@TableName(value ="message")
@Data
public class Message {
    private Long messageId;

    private Long conversationId;

    private Long senderUid;

    private Long receiverUid;

    private Integer msgType;

    private String content;

    private Integer isRecalled;

    private Date createTime;
}