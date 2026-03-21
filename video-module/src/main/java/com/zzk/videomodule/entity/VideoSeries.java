package com.zzk.videomodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName video_series
 */
@TableName(value ="video_series")
@Data
public class VideoSeries {
    @TableId(value = "series_id", type = IdType.AUTO)
    private Long seriesId;

    private Long uid;

    private String title;

    private String description;

    private String coverUrl;

    private Integer visible;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
