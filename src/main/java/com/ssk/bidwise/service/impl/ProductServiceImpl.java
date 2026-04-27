package com.ssk.bidwise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssk.bidwise.common.exception.BusinessException;
import com.ssk.bidwise.common.exception.ErrorCode;
import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.model.converter.ProductConverter;
import com.ssk.bidwise.model.dto.CreateProductRequest;
import com.ssk.bidwise.model.dto.UpdateProductRequest;
import com.ssk.bidwise.model.entity.Product;
import com.ssk.bidwise.model.vo.ProductVO;
import com.ssk.bidwise.mapper.ProductMapper;
import com.ssk.bidwise.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 商品服务实现
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductConverter productConverter;

    @Override
    public PageVO<ProductVO> pageQuery(Integer page, Integer size, String keyword) {
        Page<Product> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Product::getName, keyword);
        }
        wrapper.orderByDesc(Product::getCreateTime);
        Page<Product> result = productMapper.selectPage(pageParam, wrapper);

        PageVO<ProductVO> pageVO = new PageVO<>();
        pageVO.setPage(page);
        pageVO.setSize(size);
        pageVO.setTotal(result.getTotal());
        pageVO.setList(result.getRecords().stream()
                .map(productConverter::toVO)
                .toList());
        return pageVO;
    }

    @Override
    public ProductVO getDetail(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_EXIST, "商品不存在");
        }
        return productConverter.toVO(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductVO create(CreateProductRequest request) {
        Product product = productConverter.toEntity(request);
        productMapper.insert(product);
        return productConverter.toVO(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductVO update(UpdateProductRequest request) {
        Product existing = productMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_EXIST, "商品不存在");
        }
        existing.setName(request.getName());
        existing.setPrice(request.getPrice());
        existing.setStock(request.getStock());
        productMapper.updateById(existing);
        return productConverter.toVO(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_EXIST, "商品不存在");
        }
        productMapper.deleteById(id);
    }
}
