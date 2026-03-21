package com.zzk.common.exception;

import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.model.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，统一转换为 ApiResponse 输出。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException ex) {
        log.warn("Business exception caught: code={}, message={}", ex.getCode(), ex.getMessage(), ex);
        return ApiResponse.failure(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ApiResponse<Void> handleUnauthorizedException(UnauthorizedException ex) {
        log.warn("Unauthorized exception caught: {}", ex.getMessage(), ex);
        return ApiResponse.failure(ApiCodeEnum.UNAUTHORIZED.getCode(), ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ApiResponse<Void> handleForbiddenException(ForbiddenException ex) {
        log.warn("Forbidden exception caught: {}", ex.getMessage(), ex);
        return ApiResponse.failure(ApiCodeEnum.FORBIDDEN.getCode(), ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public ApiResponse<Void> handleValidationException(Exception ex) {
        log.warn("Validation exception caught: {}", ex.getMessage(), ex);
        return ApiResponse.failure(ApiCodeEnum.BAD_REQUEST.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception ex) {
        log.error("Unhandled exception caught by global handler", ex);
        return ApiResponse.failure(ApiCodeEnum.SYSTEM_ERROR.getCode(), ApiCodeEnum.SYSTEM_ERROR.getMessage());
    }
}
