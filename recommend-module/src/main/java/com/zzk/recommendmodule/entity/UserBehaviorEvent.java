package com.zzk.recommendmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user_behavior_event
 */
@TableName(value ="user_behavior_event")
@Data
public class UserBehaviorEvent {
    @TableId(value = "event_id", type = IdType.AUTO)
    private Long eventId;

    private Long uid;

    private Integer objType;

    private Long objId;

    private String eventType;

    private String eventValue;

    private String scene;

    private String pageFrom;

    private String deviceType;

    private Date clientTime;

    private Date createTime;
}
