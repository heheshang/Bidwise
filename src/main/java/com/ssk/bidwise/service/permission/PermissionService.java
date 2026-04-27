package com.ssk.bidwise.service.permission;

import com.ssk.bidwise.model.vo.permission.assign.PermissionAssignRoleMenuReqVO;
import com.ssk.bidwise.model.vo.permission.assign.PermissionAssignUserRoleReqVO;
import com.ssk.bidwise.model.vo.permission.assign.PermissionRoleMenuListRespVO;

import java.util.List;

/**
 * 权限分配 Service 接口
 *
 * @author Bidwise
 */
public interface PermissionService {

    /**
     * 分配角色菜单
     *
     * @param reqVO 分配信息
     */
    void assignRoleMenu(PermissionAssignRoleMenuReqVO reqVO);

    /**
     * 获得角色菜单列表
     *
     * @param roleId 角色编号
     * @return 角色菜单列表响应
     */
    PermissionRoleMenuListRespVO getRoleMenuList(Long roleId);

    /**
     * 分配用户角色
     *
     * @param reqVO 分配信息
     */
    void assignUserRole(PermissionAssignUserRoleReqVO reqVO);

    /**
     * 获得用户角色编号列表
     *
     * @param userId 用户编号
     * @return 角色编号列表
     */
    List<Long> getUserRoleIdList(Long userId);

}
