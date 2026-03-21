package com.zzk.common.exception;

/**
 * 无权限访问异常。
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
