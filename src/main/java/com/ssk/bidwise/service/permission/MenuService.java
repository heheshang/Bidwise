package com.ssk.bidwise.service.permission;

import com.ssk.bidwise.dal.dataobject.permission.MenuDO;
import com.ssk.bidwise.model.vo.permission.menu.MenuRespVO;
import com.ssk.bidwise.model.vo.permission.menu.MenuSaveReqVO;

import java.util.List;

/**
 * 菜单 Service 接口
 *
 * @author Bidwise
 */
public interface MenuService {

    /**
     * 创建菜单
     *
     * @param reqVO 创建信息
     * @return 菜单编号
     */
    Long createMenu(MenuSaveReqVO reqVO);

    /**
     * 更新菜单
     *
     * @param reqVO 更新信息
     */
    void updateMenu(MenuSaveReqVO reqVO);

    /**
     * 删除菜单
     *
     * @param id 菜单编号
     */
    void deleteMenu(Long id);

    /**
     * 获得菜单
     *
     * @param id 菜单编号
     * @return 菜单
     */
    MenuRespVO getMenu(Long id);

    /**
     * 获得菜单列表，基于用户权限
     *
     * @return 菜单树形列表
     */
    List<MenuRespVO> getMenuList();

    /**
     * 获得菜单精简列表（树形，用于下拉框选择）
     *
     * @return 菜单树形列表
     */
    List<MenuRespVO> getMenuSimpleList();

    /**
     * 获得菜单 DO
     *
     * @param id 菜单编号
     * @return 菜单 DO
     */
    MenuDO getMenuDO(Long id);

}
