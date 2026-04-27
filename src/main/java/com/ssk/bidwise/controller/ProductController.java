package com.ssk.bidwise.controller;

import com.ssk.bidwise.common.result.Result;
import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.model.dto.CreateProductRequest;
import com.ssk.bidwise.model.dto.UpdateProductRequest;
import com.ssk.bidwise.model.vo.ProductVO;
import com.ssk.bidwise.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品管理接口
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 分页查询商品列表
     */
    @GetMapping
    public Result<PageVO<ProductVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        PageVO<ProductVO> result = productService.pageQuery(page, size, keyword);
        return Result.success(result);
    }

    /**
     * 获取商品详情
     */
    @GetMapping("/{id}")
    public Result<ProductVO> getById(@PathVariable Long id) {
        ProductVO product = productService.getDetail(id);
        return Result.success(product);
    }

    /**
     * 创建商品
     */
    @PostMapping
    public Result<ProductVO> create(@Valid @RequestBody CreateProductRequest request) {
        ProductVO product = productService.create(request);
        return Result.success(product);
    }

    /**
     * 更新商品
     */
    @PutMapping("/{id}")
    public Result<ProductVO> update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        request.setId(id);
        ProductVO product = productService.update(request);
        return Result.success(product);
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success();
    }
}
