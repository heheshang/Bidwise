package com.ssk.bidwise.service.permission;

import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.dal.dataobject.permission.RoleDO;
import com.ssk.bidwise.model.vo.permission.role.RolePageReqVO;
import com.ssk.bidwise.model.vo.permission.role.RoleRespVO;
import com.ssk.bidwise.model.vo.permission.role.RoleSaveReqVO;

import java.util.List;

/**
 * 角色 Service 接口
 *
 * @author Bidwise
 */
public interface RoleService {

    /**
     * 创建角色
     *
     * @param reqVO 创建信息
     * @return 角色编号
     */
    Long createRole(RoleSaveReqVO reqVO);

    /**
     * 更新角色
     *
     * @param reqVO 更新信息
     */
    void updateRole(RoleSaveReqVO reqVO);

    /**
     * 删除角色
     *
     * @param id 角色编号
     */
    void deleteRole(Long id);

    /**
     * 获得角色
     *
     * @param id 角色编号
     * @return 角色
     */
    RoleRespVO getRole(Long id);

    /**
     * 获得角色分页
     *
     * @param pageReqVO 分页条件
     * @return 角色分页
     */
    PageVO<RoleRespVO> getRolePage(RolePageReqVO pageReqVO);

    /**
     * 获得角色精简列表（下拉框使用）
     *
     * @return 角色列表
     */
    List<RoleRespVO> getRoleSimpleList();

    /**
     * 更新角色状态
     *
     * @param id 角色编号
     * @param status 状态
     */
    void updateRoleStatus(Long id, Integer status);

    /**
     * 获得角色 DO
     *
     * @param id 角色编号
     * @return 角色 DO
     */
    RoleDO getRoleDO(Long id);

}
