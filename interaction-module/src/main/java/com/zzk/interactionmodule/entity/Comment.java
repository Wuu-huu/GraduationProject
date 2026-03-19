package com.zzk.interactionmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment {
    private Long commentId;

    private Long vid;

    private Long uid;

    private Long rootId;

    private Long parentId;

    private Long replyToUid;

    private String content;

    private Integer status;

    private Integer isTop;

    private Integer likeCount;

    private Integer replyCount;

    private Date createTime;

    private Date updateTime;
}