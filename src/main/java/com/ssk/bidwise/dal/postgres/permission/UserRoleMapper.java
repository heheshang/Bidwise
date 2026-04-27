package com.ssk.bidwise.dal.postgres.permission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssk.bidwise.dal.dataobject.permission.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联 Mapper
 *
 * @author Bidwise
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {
}
