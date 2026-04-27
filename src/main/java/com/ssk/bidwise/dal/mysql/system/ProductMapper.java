package com.ssk.bidwise.dal.mysql.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssk.bidwise.dal.dataobject.system.ProductDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品 Mapper
 */
@Mapper
public interface ProductMapper extends BaseMapper<ProductDO> {
}
