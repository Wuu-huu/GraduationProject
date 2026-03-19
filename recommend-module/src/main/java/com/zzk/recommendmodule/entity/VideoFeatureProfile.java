package com.zzk.recommendmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName video_feature_profile
 */
@TableName(value ="video_feature_profile")
@Data
public class VideoFeatureProfile {
    private Long vid;

    private Long categoryId;

    private Object tagVector;

    private BigDecimal contentQualityScore;

    private BigDecimal ctrScore;

    private BigDecimal cvrScore;

    private BigDecimal watchScore;

    private BigDecimal authorWeight;

    private String embeddingId;

    private Date updateTime;
}