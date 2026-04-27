package com.ssk.bidwise.dal.mysql.oauth2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2RefreshTokenDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * OAuth2 刷新令牌 Mapper
 */
@Mapper
public interface OAuth2RefreshTokenMapper extends BaseMapper<OAuth2RefreshTokenDO> {
}
