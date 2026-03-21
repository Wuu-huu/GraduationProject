package com.zzk.messagemodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName notification
 */
@TableName(value ="notification")
@Data
public class Notification {
    @TableId(value = "notification_id", type = IdType.AUTO)
    private Long notificationId;

    private Long uid;

    private Integer noticeType;

    private Integer bizType;

    private Long bizId;

    private String title;

    private String content;

    private Integer isRead;

    private Date createTime;
}
