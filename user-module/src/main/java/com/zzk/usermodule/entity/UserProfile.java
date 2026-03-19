package com.zzk.usermodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user_profile
 */
@TableName(value ="user_profile")
@Data
public class UserProfile {
    private Long uid;

    private String nickname;

    private String avatarUrl;

    private String backgroundUrl;

    private Integer gender;

    private Date birthday;

    private String signature;

    private String province;

    private String city;

    private Integer authType;

    private String authDesc;

    private Date updateTime;
}