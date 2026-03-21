package com.zzk.authmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户认证主表实体，严格映射 user_info。
 */
@Data
@TableName("user_info")
public class UserInfo {

    @TableId(value = "uid", type = IdType.AUTO)
    private Long uid;

    private String username;

    @TableField("password_hash")
    private String passwordHash;

    private String email;

    private String phone;

    private Integer state;

    private Integer role;

    @TableField("register_time")
    private LocalDateTime registerTime;

    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
}
