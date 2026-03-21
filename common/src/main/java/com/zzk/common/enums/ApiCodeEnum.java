package com.zzk.common.enums;

import lombok.Getter;

/**
 * 全局接口返回码定义。
 */
@Getter
public enum ApiCodeEnum {

    SUCCESS(0, "success"),
    BAD_REQUEST(10001, "请求参数错误"),
    UNAUTHORIZED(20000, "未登录或登录已失效"),
    FORBIDDEN(20001, "无权访问"),
    USERNAME_EXISTS(30001, "用户名已存在"),
    ACCOUNT_NOT_FOUND(30002, "账号不存在"),
    PASSWORD_ERROR(30003, "用户名或密码错误"),
    ACCOUNT_BANNED(30004, "账号已封禁"),
    SYSTEM_ERROR(50000, "系统异常，请稍后重试");

    private final int code;
    private final String message;

    ApiCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
