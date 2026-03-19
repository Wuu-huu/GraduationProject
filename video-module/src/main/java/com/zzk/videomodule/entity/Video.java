package com.zzk.videomodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName video
 */
@TableName(value ="video")
@Data
public class Video {
    private Long vid;

    private Long uid;

    private String title;

    private String subtitle;

    private Integer sourceType;

    private Integer copyrightType;

    private String coverUrl;

    private String description;

    private Integer status;

    private Integer publishStatus;

    private Integer visibility;

    private Integer durationSec;

    private BigDecimal qualityScore;

    private Date publishTime;

    private Date uploadTime;

    private Date deleteTime;
}