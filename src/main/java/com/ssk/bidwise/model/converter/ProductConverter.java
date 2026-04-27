package com.ssk.bidwise.model.converter;

import com.ssk.bidwise.dal.dataobject.system.ProductDO;
import com.ssk.bidwise.model.dto.CreateProductRequest;
import com.ssk.bidwise.model.dto.UpdateProductRequest;
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
    public ProductDO toEntity(CreateProductRequest request) {
        ProductDO product = new ProductDO();
        product.setCategoryId(request.getCategoryId());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setStatus(1);
        return product;
    }

    /**
     * 更新请求转实体
     */
    public void updateEntity(ProductDO product, UpdateProductRequest request) {
        if (request.getCategoryId() != null) {
            product.setCategoryId(request.getCategoryId());
        }
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }
        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }
    }

    /**
     * 实体转 VO
     */
    public ProductVO toVO(ProductDO entity) {
        ProductVO vo = new ProductVO();
        vo.setId(entity.getId());
        vo.setUserId(entity.getUserId());
        vo.setCategoryId(entity.getCategoryId());
        vo.setName(entity.getName());
        vo.setDescription(entity.getDescription());
        vo.setPrice(entity.getPrice());
        vo.setStock(entity.getStock());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }
}
