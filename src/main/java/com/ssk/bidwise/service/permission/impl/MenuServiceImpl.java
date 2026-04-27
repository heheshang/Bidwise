package com.ssk.bidwise.service.permission.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ssk.bidwise.common.exception.BusinessException;
import com.ssk.bidwise.common.exception.ErrorCode;
import com.ssk.bidwise.controller.admin.permission.vo.menu.MenuRespVO;
import com.ssk.bidwise.controller.admin.permission.vo.menu.MenuSaveReqVO;
import com.ssk.bidwise.converter.permission.MenuConverter;
import com.ssk.bidwise.dal.dataobject.permission.MenuDO;
import com.ssk.bidwise.dal.postgres.permission.MenuMapper;
import com.ssk.bidwise.dal.postgres.permission.RoleMenuMapper;
import com.ssk.bidwise.dal.redis.permission.RoleMenuRedisCache;
import com.ssk.bidwise.service.permission.MenuService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单 Service 实现
 *
 * @author Bidwise
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    @Resource
    private final MenuMapper menuMapper;

    @Resource
    private final RoleMenuMapper roleMenuMapper;

    @Resource
    private final RoleMenuRedisCache roleMenuRedisCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMenu(MenuSaveReqVO reqVO) {
        // 校验权限标识唯一
        validatePermissionUnique(null, reqVO.getPermission());
        // 校验父菜单
        validateParent(reqVO.getParentId());

        MenuDO menu = MenuConverter.INSTANCE.convert(reqVO);
        menuMapper.insert(menu);

        // 清除缓存
        roleMenuRedisCache.clearAll();

        return menu.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(MenuSaveReqVO reqVO) {
        // 校验存在
        MenuDO existing = validateMenuExists(reqVO.getId());
        // 校验权限标识唯一
        validatePermissionUnique(reqVO.getId(), reqVO.getPermission());
        // 校验父菜单
        validateParent(reqVO.getParentId());
        // 不能选自己作为父菜单
        if (reqVO.getParentId() != null && reqVO.getParentId().equals(reqVO.getId())) {
            throw new BusinessException(ErrorCode.MENU_PARENT_INVALID, "不能选择自己作为父菜单");
        }

        MenuDO menu = MenuConverter.INSTANCE.convert(reqVO);
        menu.setId(reqVO.getId());
        menuMapper.updateById(menu);

        // 清除缓存
        roleMenuRedisCache.clearAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        // 校验存在
        validateMenuExists(id);
        // 检查是否有子菜单
        LambdaQueryWrapper<MenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MenuDO::getParentId, id);
        long count = menuMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.MENU_HAS_CHILDREN, "存在子菜单，不能删除");
        }

        menuMapper.deleteById(id);

        // 删除关联的角色菜单关系
        LambdaQueryWrapper<com.ssk.bidwise.dal.dataobject.permission.RoleMenuDO> roleMenuWrapper = new LambdaQueryWrapper<>();
        roleMenuWrapper.eq(com.ssk.bidwise.dal.dataobject.permission.RoleMenuDO::getMenuId, id);
        roleMenuMapper.delete(roleMenuWrapper);

        // 清除缓存
        roleMenuRedisCache.clearAll();
    }

    @Override
    public MenuRespVO getMenu(Long id) {
        MenuDO menu = menuMapper.selectById(id);
        return MenuConverter.INSTANCE.convert(menu);
    }

    @Override
    public List<MenuRespVO> getMenuList() {
        LambdaQueryWrapper<MenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MenuDO::getStatus, 0);
        wrapper.orderByAsc(MenuDO::getParentId, MenuDO::getSort);
        List<MenuDO> menus = menuMapper.selectList(wrapper);

        return buildMenuTree(menus);
    }

    @Override
    public List<MenuRespVO> getMenuSimpleList() {
        LambdaQueryWrapper<MenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MenuDO::getStatus, 0);
        wrapper.orderByAsc(MenuDO::getParentId, MenuDO::getSort);
        List<MenuDO> menus = menuMapper.selectList(wrapper);

        return buildMenuTree(menus);
    }

    @Override
    public MenuDO getMenuDO(Long id) {
        return menuMapper.selectById(id);
    }

    private MenuDO validateMenuExists(Long id) {
        MenuDO menu = menuMapper.selectById(id);
        if (menu == null) {
            throw new BusinessException(ErrorCode.MENU_NOT_EXIST, "菜单不存在");
        }
        return menu;
    }

    private void validatePermissionUnique(Long id, String permission) {
        if (permission == null) {
            return;
        }
        LambdaQueryWrapper<MenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MenuDO::getPermission, permission);
        if (id != null) {
            wrapper.ne(MenuDO::getId, id);
        }
        MenuDO existing = menuMapper.selectOne(wrapper);
        if (existing != null) {
            throw new BusinessException(ErrorCode.MENU_PERMISSION_DUPLICATE, "权限标识 " + permission + " 已存在");
        }
    }

    private void validateParent(Long parentId) {
        if (parentId == null || parentId == 0) {
            return;
        }
        MenuDO parent = menuMapper.selectById(parentId);
        if (parent == null) {
            throw new BusinessException(ErrorCode.MENU_PARENT_NOT_EXIST, "父菜单不存在");
        }
    }

    private List<MenuRespVO> buildMenuTree(List<MenuDO> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return new ArrayList<>();
        }

        // 转换为 VO
        List<MenuRespVO> voList = menus.stream()
                .map(MenuConverter.INSTANCE::convert)
                .sorted(Comparator.comparingInt(MenuRespVO::getSort))
                .collect(Collectors.toList());

        // 构建树形结构
        List<MenuRespVO> result = new ArrayList<>();
        for (MenuRespVO vo : voList) {
            if (vo.getParentId() == null || vo.getParentId() == 0) {
                result.add(vo);
            } else {
                for (MenuRespVO parent : voList) {
                    if (parent.getId().equals(vo.getParentId())) {
                        if (parent.getChildren() == null) {
                            parent.setChildren(new ArrayList<>());
                        }
                        parent.getChildren().add(vo);
                        break;
                    }
                }
            }
        }

        return result;
    }

}
