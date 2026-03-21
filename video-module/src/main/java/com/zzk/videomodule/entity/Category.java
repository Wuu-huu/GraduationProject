package com.zzk.videomodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName category
 */
@TableName(value ="category")
@Data
public class Category {
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    private Long parentId;

    private String categoryCode;

    private String categoryName;

    private Integer level;

    private Integer sortNo;

    private Integer status;

    private String description;
}
