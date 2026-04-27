package com.ssk.bidwise.dal.postgres.permission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssk.bidwise.dal.dataobject.permission.RoleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色 Mapper
 *
 * @author Bidwise
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleDO> {
}
