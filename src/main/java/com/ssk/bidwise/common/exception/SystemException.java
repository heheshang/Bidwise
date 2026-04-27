package com.ssk.bidwise.common.exception;

/**
 * 系统异常
 * 用于系统级错误，如数据库连接失败、第三方服务调用失败、IO错误等
 */
public class SystemException extends RuntimeException {

    private final int code;

    public SystemException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public SystemException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
