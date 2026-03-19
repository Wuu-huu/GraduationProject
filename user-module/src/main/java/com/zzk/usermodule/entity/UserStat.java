package com.zzk.usermodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName user_stat
 */
@TableName(value ="user_stat")
@Data
public class UserStat {
    private Long uid;

    private Integer fansCount;

    private Integer followingCount;

    private Long likeReceivedCount;

    private Integer videoCount;

    private Long playReceivedCount;
}