package com.ssk.bidwise.service;

import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.model.dto.CreateUserRequest;
import com.ssk.bidwise.model.dto.UpdateUserRequest;
import com.ssk.bidwise.model.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 分页查询用户列表
     *
     * @param page 页码
     * @param size 每页大小
     * @param keyword 搜索关键词
     * @return 分页结果
     */
    PageVO<UserVO> pageQuery(Integer page, Integer size, String keyword);

    /**
     * 获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    UserVO getDetail(Long id);

    /**
     * 创建用户
     *
     * @param request 创建请求
     * @return 创建结果
     */
    UserVO create(CreateUserRequest request);

    /**
     * 更新用户
     *
     * @param request 更新请求
     * @return 更新结果
     */
    UserVO update(UpdateUserRequest request);

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    void delete(Long id);
}
