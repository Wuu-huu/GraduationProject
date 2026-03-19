package com.zzk.videomodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName video_part
 */
@TableName(value ="video_part")
@Data
public class VideoPart {
    private Long partId;

    private Long vid;

    private Integer partNo;

    private String title;

    private String videoUrl;

    private Integer durationSec;

    private Long sizeBytes;

    private Integer status;

    private Date createTime;
}