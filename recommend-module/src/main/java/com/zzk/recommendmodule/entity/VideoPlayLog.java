package com.zzk.recommendmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName video_play_log
 */
@TableName(value ="video_play_log")
@Data
public class VideoPlayLog {
    @TableId(value = "play_id", type = IdType.AUTO)
    private Long playId;

    private Long uid;

    private Long vid;

    private Long partId;

    private String requestId;

    private String scene;

    private String enterSource;

    private Date playStartTime;

    private Date playEndTime;

    private Integer watchSec;

    private Integer progressSec;

    private BigDecimal completionRate;

    private Integer isFinish;

    private Integer isAutoplay;

    private String deviceType;
}
