package com.ssk.bidwise.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应 VO
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> {

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 总数
     */
    private long total;

    /**
     * 当前页码
     */
    private int page;

    /**
     * 每页大小
     */
    private int size;
}
