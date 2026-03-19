package com.zzk.usermodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user_follow
 */
@TableName(value ="user_follow")
@Data
public class UserFollow {
    private Long id;

    private Long followerUid;

    private Long followeeUid;

    private Integer state;

    private Date createTime;
}