package com.zzk.usermodule.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户设置实体，映射 user_setting。
 */
@Data
@TableName("user_setting")
public class UserSetting {

    @TableId("uid")
    private Long uid;

    private Integer openRecommend;

    private Integer openPush;

    private Integer openDm;

    private Integer openFollowVisible;

    private Integer openFavoriteVisible;

    private LocalDateTime updateTime;
}
