package com.ssk.bidwise.dal.postgres.permission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssk.bidwise.dal.dataobject.permission.RoleMenuDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色菜单关联 Mapper
 *
 * @author Bidwise
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenuDO> {
}
