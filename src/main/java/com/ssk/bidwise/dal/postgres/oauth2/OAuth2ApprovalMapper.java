package com.ssk.bidwise.dal.postgres.oauth2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2ApprovalDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * OAuth2 授权批准 Mapper
 */
@Mapper
public interface OAuth2ApprovalMapper extends BaseMapper<OAuth2ApprovalDO> {
}
