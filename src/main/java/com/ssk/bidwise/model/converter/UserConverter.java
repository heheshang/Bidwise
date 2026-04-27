package com.ssk.bidwise.model.converter;

import com.ssk.bidwise.dal.dataobject.system.UserDO;
import com.ssk.bidwise.model.dto.CreateUserRequest;
import com.ssk.bidwise.model.dto.UpdateUserRequest;
import com.ssk.bidwise.model.vo.UserVO;
import org.springframework.stereotype.Component;

/**
 * 用户对象转换器
 */
@Component
public class UserConverter {

    /**
     * 创建请求转实体
     */
    public UserDO toEntity(CreateUserRequest request) {
        UserDO user = new UserDO();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setAvatar(request.getAvatar());
        user.setStatus(1);
        return user;
    }

    /**
     * 更新请求转实体
     */
    public void updateEntity(UserDO user, UpdateUserRequest request) {
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
    }

    /**
     * 实体转 VO
     */
    public UserVO toVO(UserDO entity) {
        UserVO vo = new UserVO();
        vo.setId(entity.getId());
        vo.setUsername(entity.getUsername());
        vo.setNickname(entity.getNickname());
        vo.setEmail(entity.getEmail());
        vo.setAvatar(entity.getAvatar());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }
}
