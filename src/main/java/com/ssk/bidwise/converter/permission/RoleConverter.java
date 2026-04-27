package com.ssk.bidwise.converter.permission;

import com.ssk.bidwise.dal.dataobject.permission.RoleDO;
import com.ssk.bidwise.model.vo.permission.role.RoleRespVO;
import com.ssk.bidwise.model.vo.permission.role.RoleSaveReqVO;
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
