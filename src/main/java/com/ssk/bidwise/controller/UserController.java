package com.ssk.bidwise.controller;

import com.ssk.bidwise.common.result.Result;
import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.model.dto.CreateUserRequest;
import com.ssk.bidwise.model.dto.UpdateUserRequest;
import com.ssk.bidwise.model.vo.UserVO;
import com.ssk.bidwise.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理接口
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 分页查询用户列表
     */
    @GetMapping
    public Result<PageVO<UserVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        PageVO<UserVO> result = userService.pageQuery(page, size, keyword);
        return Result.success(result);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public Result<UserVO> getById(@PathVariable Long id) {
        UserVO user = userService.getDetail(id);
        return Result.success(user);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Result<UserVO> create(@Valid @RequestBody CreateUserRequest request) {
        UserVO user = userService.create(request);
        return Result.success(user);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<UserVO> update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        request.setId(id);
        UserVO user = userService.update(request);
        return Result.success(user);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }
}
