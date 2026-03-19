package com.zzk.videomodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @TableName video_tag
 */
@TableName(value ="video_tag")
@Data
public class VideoTag {
    private Long id;

    private Long vid;

    private Long tagId;

    private Integer source;

    private BigDecimal weight;
}