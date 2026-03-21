package com.zzk.interactionmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName comment_action
 */
@TableName(value ="comment_action")
@Data
public class CommentAction {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long uid;

    private Long commentId;

    private Integer actionType;

    private Date actionTime;

    private Integer cancelFlag;
}
