package com.ssk.bidwise.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新用户请求 DTO
 */
@Data
public class UpdateUserRequest {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 255, message = "姓名长度不能超过255")
    private String name;

    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 150, message = "年龄不能超过150")
    private Integer age;

    private String gender;
}
