package com.ssk.bidwise.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新商品请求 DTO
 */
@Data
public class UpdateProductRequest {

    @NotNull(message = "商品 ID 不能为空")
    private Long id;

    private Long categoryId;

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 255, message = "商品名称长度不能超过 255")
    private String name;

    @Size(max = 65535, message = "商品描述太长")
    private String description;

    @Min(value = 0, message = "商品价格不能小于 0")
    private BigDecimal price;

    @Min(value = 0, message = "库存不能小于 0")
    private Integer stock;

    private Integer status;
}
