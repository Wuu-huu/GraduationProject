package com.zzk.common.exception;

import com.zzk.common.enums.ApiCodeEnum;
import lombok.Getter;

/**
 * 业务异常，统一携带错误码返回给前端。
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(ApiCodeEnum apiCodeEnum) {
        super(apiCodeEnum.getMessage());
        this.code = apiCodeEnum.getCode();
    }

    public BusinessException(ApiCodeEnum apiCodeEnum, String message) {
        super(message);
        this.code = apiCodeEnum.getCode();
    }
}
