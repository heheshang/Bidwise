package com.ssk.bidwise.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建商品请求 DTO
 */
@Data
public class CreateProductRequest {

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 255, message = "商品名称长度不能超过255")
    private String name;

    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格不能小于0")
    private BigDecimal price;

    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存不能小于0")
    private Integer stock;
}
