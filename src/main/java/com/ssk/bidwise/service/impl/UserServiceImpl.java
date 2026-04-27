package com.ssk.bidwise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssk.bidwise.common.exception.BusinessException;
import com.ssk.bidwise.common.exception.ErrorCode;
import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.dal.dataobject.system.UserDO;
import com.ssk.bidwise.dal.mysql.system.UserMapper;
import com.ssk.bidwise.model.converter.UserConverter;
import com.ssk.bidwise.model.dto.CreateUserRequest;
import com.ssk.bidwise.model.dto.UpdateUserRequest;
import com.ssk.bidwise.model.vo.UserVO;
import com.ssk.bidwise.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserConverter userConverter;

    @Override
    public PageVO<UserVO> pageQuery(Integer page, Integer size, String keyword) {
        Page<UserDO> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(UserDO::getUsername, keyword);
        }
        wrapper.orderByDesc(UserDO::getCreateTime);
        Page<UserDO> result = userMapper.selectPage(pageParam, wrapper);

        PageVO<UserVO> pageVO = new PageVO<>();
        pageVO.setPage(page);
        pageVO.setSize(size);
        pageVO.setTotal(result.getTotal());
        pageVO.setList(result.getRecords().stream()
                .map(userConverter::toVO)
                .toList());
        return pageVO;
    }

    @Override
    public UserVO getDetail(Long id) {
        UserDO user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_EXIST, "用户不存在");
        }
        return userConverter.toVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO create(CreateUserRequest request) {
        UserDO user = userConverter.toEntity(request);
        userMapper.insert(user);
        return userConverter.toVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO update(UpdateUserRequest request) {
        UserDO existing = userMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_EXIST, "用户不存在");
        }
        existing.setUsername(request.getUsername());
        userConverter.updateEntity(existing, request);
        userMapper.updateById(existing);
        return userConverter.toVO(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        UserDO user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_EXIST, "用户不存在");
        }
        userMapper.deleteById(id);
    }
}
