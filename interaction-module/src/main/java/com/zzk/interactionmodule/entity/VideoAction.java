package com.zzk.interactionmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName video_action
 */
@TableName(value ="video_action")
@Data
public class VideoAction {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long uid;

    private Long vid;

    private Integer actionType;

    private Integer actionValue;

    private Date actionTime;

    private Integer cancelFlag;
}
