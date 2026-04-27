package com.ssk.bidwise.service;

import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.model.dto.CreateProductRequest;
import com.ssk.bidwise.model.dto.UpdateProductRequest;
import com.ssk.bidwise.model.vo.ProductVO;

/**
 * 商品服务接口
 */
public interface ProductService {

    /**
     * 分页查询商品列表
     *
     * @param page 页码
     * @param size 每页大小
     * @param keyword 搜索关键词
     * @return 分页结果
     */
    PageVO<ProductVO> pageQuery(Integer page, Integer size, String keyword);

    /**
     * 获取商品详情
     *
     * @param id 商品ID
     * @return 商品详情
     */
    ProductVO getDetail(Long id);

    /**
     * 创建商品
     *
     * @param request 创建请求
     * @return 创建结果
     */
    ProductVO create(CreateProductRequest request);

    /**
     * 更新商品
     *
     * @param request 更新请求
     * @return 更新结果
     */
    ProductVO update(UpdateProductRequest request);

    /**
     * 删除商品
     *
     * @param id 商品ID
     */
    void delete(Long id);
}
