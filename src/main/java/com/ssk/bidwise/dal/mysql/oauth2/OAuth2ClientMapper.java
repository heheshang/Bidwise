package com.ssk.bidwise.dal.mysql.oauth2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2ClientDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * OAuth2 客户端 Mapper
 */
@Mapper
public interface OAuth2ClientMapper extends BaseMapper<OAuth2ClientDO> {
}
