package com.zzk.usermodule.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户统计实体，映射 user_stat。
 */
@Data
@TableName("user_stat")
public class UserStat {

    @TableId("uid")
    private Long uid;

    private Integer fansCount;

    private Integer followingCount;

    private Long likeReceivedCount;

    private Integer videoCount;

    private Long playReceivedCount;
}
