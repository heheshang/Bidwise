package com.ssk.bidwise.common.exception;

/**
 * 错误码枚举定义
 * 错误码分段：
 * 1xxx - 参数校验错误
 * 2xxx - 业务错误
 * 3xxx - 权限相关错误
 * 4xxx - 资源不存在
 * 5xxx - 系统错误
 */
public enum ErrorCode {

    // 1xxx - 参数校验错误
    PARAM_ERROR(1001, "参数错误"),
    PARAM_BLANK(1002, "参数不能为空"),
    PARAM_INVALID(1003, "参数格式不正确"),

    // 2xxx - 业务错误
    DATA_NOT_EXIST(2001, "数据不存在"),
    OPERATION_FAILED(2002, "操作失败"),

    // 3xxx - 权限错误
    UNAUTHORIZED(3001, "未授权"),
    FORBIDDEN(3002, "禁止访问"),

    // 4xxx - 资源不存在
    RESOURCE_NOT_FOUND(4001, "资源不存在"),

    // 5xxx - 系统错误
    SYSTEM_ERROR(5001, "系统内部错误"),
    DB_ERROR(5002, "数据库操作错误");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
