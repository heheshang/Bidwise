package com.ssk.bidwise.converter.permission;

import com.ssk.bidwise.dal.dataobject.permission.MenuDO;
import com.ssk.bidwise.model.vo.permission.menu.MenuRespVO;
import com.ssk.bidwise.model.vo.permission.menu.MenuSaveReqVO;
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
