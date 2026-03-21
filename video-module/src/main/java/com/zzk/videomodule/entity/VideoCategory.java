package com.zzk.videomodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName video_category
 */
@TableName(value ="video_category")
@Data
public class VideoCategory {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long vid;

    private Long categoryId;

    private Integer isPrimary;
}
