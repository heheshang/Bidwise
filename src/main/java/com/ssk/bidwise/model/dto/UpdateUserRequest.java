package com.ssk.bidwise.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新用户请求 DTO
 */
@Data
public class UpdateUserRequest {

    @NotNull(message = "用户 ID 不能为空")
    private Long id;

    @Size(min = 3, max = 64, message = "用户名长度必须在 3-64 之间")
    private String username;

    @Size(min = 6, max = 128, message = "密码长度必须在 6-128 之间")
    private String password;

    @Size(max = 100, message = "昵称长度不能超过 100")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    @Size(max = 200, message = "邮箱长度不能超过 200")
    private String email;

    @Size(max = 500, message = "头像地址长度不能超过 500")
    private String avatar;

    private Integer status;
}
