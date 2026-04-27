package com.ssk.bidwise.converter.permission;

import com.ssk.bidwise.controller.admin.permission.vo.menu.MenuRespVO;
import com.ssk.bidwise.controller.admin.permission.vo.menu.MenuSaveReqVO;
import com.ssk.bidwise.dal.dataobject.permission.MenuDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 菜单 Converter
 *
 * @author Bidwise
 */
@Mapper
public interface MenuConverter {

    MenuConverter INSTANCE = Mappers.getMapper(MenuConverter.class);

    MenuDO convert(MenuSaveReqVO reqVO);

    MenuRespVO convert(MenuDO menu);

}
