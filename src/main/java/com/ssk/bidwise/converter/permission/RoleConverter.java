package com.ssk.bidwise.converter.permission;

import com.ssk.bidwise.controller.admin.permission.vo.role.RoleRespVO;
import com.ssk.bidwise.controller.admin.permission.vo.role.RoleSaveReqVO;
import com.ssk.bidwise.dal.dataobject.permission.RoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 角色 Converter
 *
 * @author Bidwise
 */
@Mapper
public interface RoleConverter {

    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    RoleDO convert(RoleSaveReqVO reqVO);

    RoleRespVO convert(RoleDO role);

}
