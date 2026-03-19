package com.zzk.authmodule.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user_info
 */
@TableName(value ="user_info")
@Data
public class UserInfo {
    private Long uid;

    private String username;

    private String passwordHash;

    private String email;

    private String phone;

    private Integer state;

    private Integer role;

    private Date registerTime;

    private Date lastLoginTime;
}