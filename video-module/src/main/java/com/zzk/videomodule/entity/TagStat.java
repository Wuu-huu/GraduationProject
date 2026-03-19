package com.zzk.videomodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName tag_stat
 */
@TableName(value ="tag_stat")
@Data
public class TagStat {
    private Long tagId;

    private Long useCount;

    private BigDecimal hotScore;

    private Date updateTime;
}