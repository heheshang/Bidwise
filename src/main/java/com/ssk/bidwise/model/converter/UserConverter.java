package com.ssk.bidwise.model.converter;

import com.ssk.bidwise.model.dto.CreateUserRequest;
import com.ssk.bidwise.model.entity.User;
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
    public User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        return user;
    }

    /**
     * 实体转 VO
     */
    public UserVO toVO(User entity) {
        UserVO vo = new UserVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setAge(entity.getAge());
        vo.setGender(entity.getGender());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }
}
