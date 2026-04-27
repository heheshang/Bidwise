package com.ssk.bidwise.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品视图对象 VO
 */
@Data
public class ProductVO {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private LocalDateTime createTime;
}
