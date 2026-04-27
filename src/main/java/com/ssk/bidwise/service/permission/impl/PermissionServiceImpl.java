package com.ssk.bidwise.service.permission.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ssk.bidwise.common.exception.BusinessException;
import com.ssk.bidwise.common.exception.ErrorCode;
import com.ssk.bidwise.controller.admin.permission.vo.menu.MenuRespVO;
import com.ssk.bidwise.converter.permission.MenuConverter;
import com.ssk.bidwise.dal.dataobject.permission.MenuDO;
import com.ssk.bidwise.dal.dataobject.permission.RoleDO;
import com.ssk.bidwise.dal.dataobject.permission.RoleMenuDO;
import com.ssk.bidwise.dal.dataobject.permission.UserRoleDO;
import com.ssk.bidwise.dal.postgres.permission.MenuMapper;
import com.ssk.bidwise.dal.postgres.permission.RoleMapper;
import com.ssk.bidwise.dal.postgres.permission.RoleMenuMapper;
import com.ssk.bidwise.dal.postgres.permission.UserRoleMapper;
import com.ssk.bidwise.dal.redis.permission.RoleMenuRedisCache;
import com.ssk.bidwise.model.vo.permission.assign.PermissionAssignRoleMenuReqVO;
import com.ssk.bidwise.model.vo.permission.assign.PermissionAssignUserRoleReqVO;
import com.ssk.bidwise.model.vo.permission.assign.PermissionRoleMenuListRespVO;
import com.ssk.bidwise.service.permission.MenuService;
import com.ssk.bidwise.service.permission.PermissionService;
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
 * 权限分配 Service 实现
 *
 * @author Bidwise
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private final RoleMapper roleMapper;

    @Resource
    private final MenuMapper menuMapper;

    @Resource
    private final RoleMenuMapper roleMenuMapper;

    @Resource
    private final UserRoleMapper userRoleMapper;

    @Resource
    private final MenuService menuService;

    @Resource
    private final RoleMenuRedisCache roleMenuRedisCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleMenu(PermissionAssignRoleMenuReqVO reqVO) {
        // 校验角色存在
        RoleDO role = roleMapper.selectById(reqVO.getRoleId());
        if (role == null) {
            throw new BusinessException(ErrorCode.ROLE_NOT_EXIST, "角色不存在");
        }

        // 删除旧的关联
        LambdaQueryWrapper<RoleMenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenuDO::getRoleId, reqVO.getRoleId());
        roleMenuMapper.delete(wrapper);

        // 插入新的关联
        if (!CollectionUtils.isEmpty(reqVO.getMenuIds())) {
            for (Long menuId : reqVO.getMenuIds()) {
                RoleMenuDO roleMenu = new RoleMenuDO();
                roleMenu.setRoleId(reqVO.getRoleId());
                roleMenu.setMenuId(menuId);
                roleMenu.setTenantId(role.getTenantId());
                roleMenuMapper.insert(roleMenu);
            }
        }

        // 清除缓存
        roleMenuRedisCache.clearAll();
    }

    @Override
    public PermissionRoleMenuListRespVO getRoleMenuList(Long roleId) {
        // 校验角色存在
        RoleDO role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ErrorCode.ROLE_NOT_EXIST, "角色不存在");
        }

        // 查询所有启用的菜单
        LambdaQueryWrapper<MenuDO> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.eq(MenuDO::getStatus, 0);
        menuWrapper.orderByAsc(MenuDO::getParentId, MenuDO::getSort);
        List<MenuDO> allMenus = menuMapper.selectList(menuWrapper);

        // 查询该角色已分配的菜单
        LambdaQueryWrapper<RoleMenuDO> roleMenuWrapper = new LambdaQueryWrapper<>();
        roleMenuWrapper.eq(RoleMenuDO::getRoleId, roleId);
        List<RoleMenuDO> roleMenus = roleMenuMapper.selectList(roleMenuWrapper);
        List<Long> selectedMenuIds = roleMenus.stream()
                .map(RoleMenuDO::getMenuId)
                .collect(Collectors.toList());

        // 构建树形菜单
        List<MenuRespVO> menuTree = buildMenuTree(allMenus);

        // 构造返回
        PermissionRoleMenuListRespVO respVO = new PermissionRoleMenuListRespVO();
        respVO.setMenus(menuTree);
        respVO.setSelectedMenuIds(selectedMenuIds);
        return respVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserRole(PermissionAssignUserRoleReqVO reqVO) {
        // 删除旧的关联
        LambdaQueryWrapper<UserRoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRoleDO::getUserId, reqVO.getUserId());
        userRoleMapper.delete(wrapper);

        // 插入新的关联
        if (!CollectionUtils.isEmpty(reqVO.getRoleIds())) {
            for (Long roleId : reqVO.getRoleIds()) {
                UserRoleDO userRole = new UserRoleDO();
                userRole.setUserId(reqVO.getUserId());
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }

        // 清除缓存
        roleMenuRedisCache.clearAll();
    }

    @Override
    public List<Long> getUserRoleIdList(Long userId) {
        LambdaQueryWrapper<UserRoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRoleDO::getUserId, userId);
        List<UserRoleDO> userRoles = userRoleMapper.selectList(wrapper);
        return userRoles.stream()
                .map(UserRoleDO::getRoleId)
                .collect(Collectors.toList());
    }

    private List<MenuRespVO> buildMenuTree(List<MenuDO> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return new ArrayList<>();
        }

        List<MenuRespVO> voList = menus.stream()
                .map(MenuConverter.INSTANCE::convert)
                .sorted(Comparator.comparingInt(MenuRespVO::getSort))
                .collect(Collectors.toList());

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
