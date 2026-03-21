package com.zzk.usermodule.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户资料实体，映射 user_profile。
 */
@Data
@TableName("user_profile")
public class UserProfile {

    @TableId("uid")
    private Long uid;

    private String nickname;

    private String avatarUrl;

    private String backgroundUrl;

    private Integer gender;

    private LocalDate birthday;

    private String signature;

    private String province;

    private String city;

    private Integer authType;

    private String authDesc;

    private LocalDateTime updateTime;
}
