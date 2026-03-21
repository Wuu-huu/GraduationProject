package com.zzk.interactionmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName favorite_folder
 */
@TableName(value ="favorite_folder")
@Data
public class FavoriteFolder {
    @TableId(value = "fid", type = IdType.AUTO)
    private Long fid;

    private Long uid;

    private Integer folderType;

    private String title;

    private String description;

    private String coverUrl;

    private Integer visible;

    private Integer sortNo;

    private Integer itemCount;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
