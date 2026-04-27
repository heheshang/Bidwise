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
    DB_ERROR(5002, "数据库操作错误"),

    // ========== OAuth2 错误（100202xxx）==========
    OAUTH2_CLIENT_NOT_EXIST(1002020001, "OAuth2 客户端不存在"),
    OAUTH2_CLIENT_DISABLED(1002020002, "OAuth2 客户端已被禁用"),
    OAUTH2_CODE_NOT_EXIST(1002020011, "授权码不存在"),
    OAUTH2_CODE_EXPIRED(1002020012, "授权码已过期"),
    OAUTH2_REFRESH_TOKEN_NOT_EXIST(1002020021, "刷新令牌不存在"),
    OAUTH2_REFRESH_TOKEN_EXPIRED(1002020022, "刷新令牌已过期"),
    OAUTH2_REFRESH_TOKEN_INVALID(1002020023, "刷新令牌无效"),
    OAUTH2_ACCESS_TOKEN_INVALID(1002020031, "访问令牌无效或已过期"),
    OAUTH2_REDIRECT_URI_MISMATCH(1002020041, "重定向 URI 不匹配"),
    OAUTH2_GRANT_TYPE_NOT_SUPPORTED(1002020051, "不支持该授权类型"),
    OAUTH2_SCOPE_NOT_APPROVED(1002020061, "授权范围未获得用户批准"),

    // ========== RBAC 错误（100203xxx）==========
    ROLE_NOT_EXIST(1002030001, "角色不存在"),
    ROLE_CODE_DUPLICATE(1002030002, "角色编码已存在"),
    ROLE_CANNOT_MODIFY_CODE(1002030003, "内置角色编码不允许修改"),
    ROLE_CANNOT_DELETE_SYSTEM(1002030004, "内置角色不允许删除"),
    MENU_NOT_EXIST(1002030101, "菜单不存在"),
    MENU_PERMISSION_DUPLICATE(1002030102, "权限标识已存在"),
    MENU_PARENT_NOT_EXIST(1002030103, "父菜单不存在"),
    MENU_PARENT_INVALID(1002030104, "不能选择自己作为父菜单"),
    MENU_HAS_CHILDREN(1002030105, "存在子菜单，不能删除"),

    // ========== 用户错误 ==========
    USER_NOT_EXIST(2002, "用户不存在"),
    USER_PASSWORD_ERROR(2003, "用户名或密码错误"),
    USER_DISABLED(2004, "用户已被禁用");

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
