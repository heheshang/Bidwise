package com.ssk.bidwise.dal.postgres.oauth2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2CodeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * OAuth2 授权码 Mapper
 */
@Mapper
public interface OAuth2CodeMapper extends BaseMapper<OAuth2CodeDO> {
}
