package com.zzk.usermodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户关注关系实体，映射 user_follow。
 */
@Data
@TableName("user_follow")
public class UserFollow {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long followerUid;

    private Long followeeUid;

    private Integer state;

    private LocalDateTime createTime;
}
