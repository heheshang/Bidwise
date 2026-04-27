package com.ssk.bidwise;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssk.bidwise.common.exception.BusinessException;
import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.model.converter.ProductConverter;
import com.ssk.bidwise.model.dto.CreateProductRequest;
import com.ssk.bidwise.model.dto.UpdateProductRequest;
import com.ssk.bidwise.model.entity.Product;
import com.ssk.bidwise.model.vo.ProductVO;
import com.ssk.bidwise.mapper.ProductMapper;
import com.ssk.bidwise.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

/**
 * 商品服务单元测试
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductConverter productConverter;

    @Test
    void shouldReturnPageResultWhenPageQueryWithKeyword() {
        // Given
        Integer page = 1;
        Integer size = 10;
        String keyword = "test";
        Page<Product> pageParam = new Page<>(page, size);
        List<Product> records = Collections.singletonList(createTestProduct());
        pageParam.setRecords(records);
        pageParam.setTotal(1);

        given(productMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .willReturn(pageParam);
        given(productConverter.toVO(any(Product.class))).willReturn(createTestProductVO());

        // When
        PageVO<ProductVO> result = productService.pageQuery(page, size, keyword);

        // Then
        assertNotNull(result);
        assertEquals(page, result.getPage());
        assertEquals(size, result.getSize());
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getList().size());
        verify(productMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void shouldReturnProductVOWhenGetDetailWithExistingId() {
        // Given
        Long id = 1L;
        Product product = createTestProduct();
        ProductVO productVO = createTestProductVO();
        given(productMapper.selectById(eq(id))).willReturn(product);
        given(productConverter.toVO(eq(product))).willReturn(productVO);

        // When
        ProductVO result = productService.getDetail(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("test", result.getName());
        verify(productMapper).selectById(eq(id));
    }

    @Test
    void shouldThrowExceptionWhenGetDetailWithNonExistentId() {
        // Given
        Long id = 999L;
        given(productMapper.selectById(eq(id))).willReturn(null);

        // When + Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.getDetail(id);
        });
        assertEquals(2001, exception.getCode());
        assertEquals("商品不存在", exception.getMessage());
    }

    @Test
    void shouldCreateProductWhenValidRequest() {
        // Given
        CreateProductRequest request = createValidCreateRequest();
        Product product = createTestProduct();
        ProductVO productVO = createTestProductVO();
        given(productConverter.toEntity(eq(request))).willReturn(product);
        given(productMapper.insert(eq(product))).willReturn(1);
        given(productConverter.toVO(eq(product))).willReturn(productVO);

        // When
        ProductVO result = productService.create(request);

        // Then
        assertNotNull(result);
        assertEquals("test", result.getName());
        verify(productMapper).insert(eq(product));
    }

    @Test
    void shouldUpdateProductWhenValidRequest() {
        // Given
        UpdateProductRequest request = createValidUpdateRequest();
        Product existing = createTestProduct();
        ProductVO productVO = createTestProductVO();
        given(productMapper.selectById(eq(request.getId()))).willReturn(existing);
        given(productMapper.updateById(eq(existing))).willReturn(1);
        given(productConverter.toVO(eq(existing))).willReturn(productVO);

        // When
        ProductVO result = productService.update(request);

        // Then
        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        verify(productMapper).updateById(eq(existing));
    }

    @Test
    void shouldThrowExceptionWhenUpdateNonExistentProduct() {
        // Given
        UpdateProductRequest request = createValidUpdateRequest();
        given(productMapper.selectById(eq(request.getId()))).willReturn(null);

        // When + Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.update(request);
        });
        assertEquals(2001, exception.getCode());
    }

    @Test
    void shouldDeleteProductWhenExistingId() {
        // Given
        Long id = 1L;
        Product product = createTestProduct();
        given(productMapper.selectById(eq(id))).willReturn(product);

        // When
        productService.delete(id);

        // Then
        then(productMapper).should().deleteById(eq(id));
    }

    @Test
    void shouldThrowExceptionWhenDeleteNonExistentProduct() {
        // Given
        Long id = 999L;
        given(productMapper.selectById(eq(id))).willReturn(null);

        // When + Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.delete(id);
        });
        assertEquals(2001, exception.getCode());
    }

    private Product createTestProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("test");
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(10);
        return product;
    }

    private ProductVO createTestProductVO() {
        ProductVO vo = new ProductVO();
        vo.setId(1L);
        vo.setName("test");
        vo.setPrice(new BigDecimal("100.00"));
        vo.setStock(10);
        return vo;
    }

    private CreateProductRequest createValidCreateRequest() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("test");
        request.setPrice(new BigDecimal("100.00"));
        request.setStock(10);
        return request;
    }

    private UpdateProductRequest createValidUpdateRequest() {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setId(1L);
        request.setName("updated");
        request.setPrice(new BigDecimal("150.00"));
        request.setStock(5);
        return request;
    }
}
