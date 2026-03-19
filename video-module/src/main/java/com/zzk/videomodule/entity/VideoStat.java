package com.zzk.videomodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName video_stat
 */
@TableName(value ="video_stat")
@Data
public class VideoStat {
    private Long vid;

    private Long exposureCount;

    private Long playCount;

    private Long likeCount;

    private Long dislikeCount;

    private Long coinCount;

    private Long favoriteCount;

    private Long shareCount;

    private Long commentCount;

    private Long danmuCount;

    private BigDecimal avgWatchSec;

    private BigDecimal avgCompletionRate;

    private BigDecimal hotScore;

    private Date updateTime;
}