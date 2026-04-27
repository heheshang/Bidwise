package com.ssk.bidwise.common.exception;

/**
 * 业务异常
 * 用于业务逻辑错误，如参数不合法、业务规则不满足、资源不存在等
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
