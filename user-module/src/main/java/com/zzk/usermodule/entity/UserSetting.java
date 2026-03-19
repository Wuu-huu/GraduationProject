package com.zzk.usermodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user_setting
 */
@TableName(value ="user_setting")
@Data
public class UserSetting {
    private Long uid;

    private Integer openRecommend;

    private Integer openPush;

    private Integer openDm;

    private Integer openFollowVisible;

    private Integer openFavoriteVisible;

    private Date updateTime;
}