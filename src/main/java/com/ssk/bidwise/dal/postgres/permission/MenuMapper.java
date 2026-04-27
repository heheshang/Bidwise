package com.ssk.bidwise.dal.postgres.permission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssk.bidwise.dal.dataobject.permission.MenuDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单 Mapper
 *
 * @author Bidwise
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuDO> {
}
