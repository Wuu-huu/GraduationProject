package com.zzk.videomodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName video_series_item
 */
@TableName(value ="video_series_item")
@Data
public class VideoSeriesItem {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long seriesId;

    private Long vid;

    private Integer sortNo;

    private Date createTime;
}
