package com.ssk.bidwise.model.converter;

import com.ssk.bidwise.model.dto.CreateProductRequest;
import com.ssk.bidwise.model.entity.Product;
import com.ssk.bidwise.model.vo.ProductVO;
import org.springframework.stereotype.Component;

/**
 * 商品对象转换器
 */
@Component
public class ProductConverter {

    /**
     * 创建请求转实体
     */
    public Product toEntity(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        return product;
    }

    /**
     * 实体转 VO
     */
    public ProductVO toVO(Product entity) {
        ProductVO vo = new ProductVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setPrice(entity.getPrice());
        vo.setStock(entity.getStock());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }
}
