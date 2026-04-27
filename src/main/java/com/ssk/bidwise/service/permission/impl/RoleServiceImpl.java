package com.ssk.bidwise.service.permission.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssk.bidwise.common.exception.BusinessException;
import com.ssk.bidwise.common.exception.ErrorCode;
import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.converter.permission.RoleConverter;
import com.ssk.bidwise.dal.dataobject.permission.RoleDO;
import com.ssk.bidwise.dal.postgres.permission.RoleMapper;
import com.ssk.bidwise.dal.redis.permission.RoleMenuRedisCache;
import com.ssk.bidwise.model.vo.permission.role.RolePageReqVO;
import com.ssk.bidwise.model.vo.permission.role.RoleRespVO;
import com.ssk.bidwise.model.vo.permission.role.RoleSaveReqVO;
import com.ssk.bidwise.service.permission.RoleService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色 Service 实现
 *
 * @author Bidwise
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    @Resource
    private final RoleMapper roleMapper;

    @Resource
    private final RoleMenuRedisCache roleMenuRedisCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleSaveReqVO reqVO) {
        // 校验角色编码唯一
        validateRoleCodeUnique(null, reqVO.getCode());

        RoleDO role = RoleConverter.INSTANCE.convert(reqVO);
        roleMapper.insert(role);

        // 清除缓存（角色变更可能影响权限）
        roleMenuRedisCache.clearAll();

        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleSaveReqVO reqVO) {
        // 校验存在
        RoleDO existing = validateRoleExists(reqVO.getId());
        // 校验编码唯一
        validateRoleCodeUnique(reqVO.getId(), reqVO.getCode());
        // 内置角色不允许修改编码
        if (existing.getType() == 1 && !existing.getCode().equals(reqVO.getCode())) {
            throw new BusinessException(ErrorCode.ROLE_CANNOT_MODIFY_CODE, "内置角色编码不允许修改");
        }

        RoleDO role = RoleConverter.INSTANCE.convert(reqVO);
        role.setId(reqVO.getId());
        roleMapper.updateById(role);

        // 清除缓存
        roleMenuRedisCache.clearAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        // 校验存在
        RoleDO role = validateRoleExists(id);
        // 内置角色不允许删除
        if (role.getType() == 1) {
            throw new BusinessException(ErrorCode.ROLE_CANNOT_DELETE_SYSTEM, "内置角色不允许删除");
        }

        roleMapper.deleteById(id);

        // 清除缓存
        roleMenuRedisCache.clearAll();
    }

    @Override
    public RoleRespVO getRole(Long id) {
        RoleDO role = roleMapper.selectById(id);
        return RoleConverter.INSTANCE.convert(role);
    }

    @Override
    public PageVO<RoleRespVO> getRolePage(RolePageReqVO pageReqVO) {
        Page<RoleDO> page = new Page<>(pageReqVO.getPage(), pageReqVO.getSize());
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(pageReqVO.getName())) {
            wrapper.like(RoleDO::getName, pageReqVO.getName());
        }
        if (StringUtils.hasText(pageReqVO.getCode())) {
            wrapper.like(RoleDO::getCode, pageReqVO.getCode());
        }
        if (pageReqVO.getStatus() != null) {
            wrapper.eq(RoleDO::getStatus, pageReqVO.getStatus());
        }
        if (pageReqVO.getCreateTime() != null && pageReqVO.getCreateTime().length == 2) {
            wrapper.between(RoleDO::getCreateTime, pageReqVO.getCreateTime()[0], pageReqVO.getCreateTime()[1]);
        }

        wrapper.orderByDesc(RoleDO::getSort);
        Page<RoleDO> result = roleMapper.selectPage(page, wrapper);

        List<RoleRespVO> list = result.getRecords().stream()
                .map(RoleConverter.INSTANCE::convert)
                .collect(Collectors.toList());

        return new PageVO<>(list, result.getTotal(), pageReqVO.getPage(), pageReqVO.getSize());
    }

    @Override
    public List<RoleRespVO> getRoleSimpleList() {
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleDO::getStatus, 0);
        wrapper.orderByAsc(RoleDO::getSort);
        List<RoleDO> roles = roleMapper.selectList(wrapper);

        return roles.stream()
                .map(RoleConverter.INSTANCE::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleStatus(Long id, Integer status) {
        RoleDO role = validateRoleExists(id);
        role.setStatus(status);
        roleMapper.updateById(role);

        // 清除缓存，状态变更影响权限
        roleMenuRedisCache.clearAll();
    }

    @Override
    public RoleDO getRoleDO(Long id) {
        return roleMapper.selectById(id);
    }

    private RoleDO validateRoleExists(Long id) {
        RoleDO role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(ErrorCode.ROLE_NOT_EXIST, "角色不存在");
        }
        return role;
    }

    private void validateRoleCodeUnique(Long id, String code) {
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleDO::getCode, code);
        if (id != null) {
            wrapper.ne(RoleDO::getId, id);
        }
        RoleDO existing = roleMapper.selectOne(wrapper);
        if (existing != null) {
            throw new BusinessException(ErrorCode.ROLE_CODE_DUPLICATE, "角色编码 " + code + " 已存在");
        }
    }

}
