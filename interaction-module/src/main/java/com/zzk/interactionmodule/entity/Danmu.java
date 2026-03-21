package com.zzk.interactionmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName danmu
 */
@TableName(value ="danmu")
@Data
public class Danmu {
    @TableId(value = "danmu_id", type = IdType.AUTO)
    private Long danmuId;

    private Long vid;

    private Long partId;

    private Long uid;

    private String content;

    private String color;

    private Integer fontSize;

    private Integer mode;

    private Integer timePointMs;

    private Integer status;

    private Date createTime;
}
