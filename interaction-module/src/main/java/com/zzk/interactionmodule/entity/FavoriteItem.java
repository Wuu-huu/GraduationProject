package com.zzk.interactionmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName favorite_item
 */
@TableName(value ="favorite_item")
@Data
public class FavoriteItem {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long fid;

    private Long vid;

    private Long uid;

    private Integer status;

    private Date createTime;
}
