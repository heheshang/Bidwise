package com.ssk.bidwise;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssk.bidwise.common.exception.BusinessException;
import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.model.converter.UserConverter;
import com.ssk.bidwise.model.dto.CreateUserRequest;
import com.ssk.bidwise.model.dto.UpdateUserRequest;
import com.ssk.bidwise.model.entity.User;
import com.ssk.bidwise.model.vo.UserVO;
import com.ssk.bidwise.mapper.UserMapper;
import com.ssk.bidwise.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

/**
 * 用户服务单元测试
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserConverter userConverter;

    @Test
    void shouldReturnPageResultWhenPageQueryWithKeyword() {
        // Given
        Integer page = 1;
        Integer size = 10;
        String keyword = "test";
        Page<User> pageParam = new Page<>(page, size);
        List<User> records = Collections.singletonList(createTestUser());
        pageParam.setRecords(records);
        pageParam.setTotal(1);

        given(userMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .willReturn(pageParam);
        given(userConverter.toVO(any(User.class))).willReturn(createTestUserVO());

        // When
        PageVO<UserVO> result = userService.pageQuery(page, size, keyword);

        // Then
        assertNotNull(result);
        assertEquals(page, result.getPage());
        assertEquals(size, result.getSize());
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getList().size());
        verify(userMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void shouldReturnUserVOWhenGetDetailWithExistingId() {
        // Given
        Long id = 1L;
        User user = createTestUser();
        UserVO userVO = createTestUserVO();
        given(userMapper.selectById(eq(id))).willReturn(user);
        given(userConverter.toVO(eq(user))).willReturn(userVO);

        // When
        UserVO result = userService.getDetail(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("test", result.getName());
        verify(userMapper).selectById(eq(id));
    }

    @Test
    void shouldThrowExceptionWhenGetDetailWithNonExistentId() {
        // Given
        Long id = 999L;
        given(userMapper.selectById(eq(id))).willReturn(null);

        // When + Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.getDetail(id);
        });
        assertEquals(2001, exception.getCode());
        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void shouldCreateUserWhenValidRequest() {
        // Given
        CreateUserRequest request = createValidCreateRequest();
        User user = createTestUser();
        UserVO userVO = createTestUserVO();
        given(userConverter.toEntity(eq(request))).willReturn(user);
        given(userMapper.insert(eq(user))).willReturn(1);
        given(userConverter.toVO(eq(user))).willReturn(userVO);

        // When
        UserVO result = userService.create(request);

        // Then
        assertNotNull(result);
        assertEquals("test", result.getName());
        verify(userMapper).insert(eq(user));
    }

    @Test
    void shouldUpdateUserWhenValidRequest() {
        // Given
        UpdateUserRequest request = createValidUpdateRequest();
        User existing = createTestUser();
        UserVO userVO = createTestUserVO();
        given(userMapper.selectById(eq(request.getId()))).willReturn(existing);
        given(userMapper.updateById(eq(existing))).willReturn(1);
        given(userConverter.toVO(eq(existing))).willReturn(userVO);

        // When
        UserVO result = userService.update(request);

        // Then
        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        verify(userMapper).updateById(eq(existing));
    }

    @Test
    void shouldThrowExceptionWhenUpdateNonExistentUser() {
        // Given
        UpdateUserRequest request = createValidUpdateRequest();
        given(userMapper.selectById(eq(request.getId()))).willReturn(null);

        // When + Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.update(request);
        });
        assertEquals(2001, exception.getCode());
    }

    @Test
    void shouldDeleteUserWhenExistingId() {
        // Given
        Long id = 1L;
        User user = createTestUser();
        given(userMapper.selectById(eq(id))).willReturn(user);

        // When
        userService.delete(id);

        // Then
        then(userMapper).should().deleteById(eq(id));
    }

    @Test
    void shouldThrowExceptionWhenDeleteNonExistentUser() {
        // Given
        Long id = 999L;
        given(userMapper.selectById(eq(id))).willReturn(null);

        // When + Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.delete(id);
        });
        assertEquals(2001, exception.getCode());
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setAge(20);
        user.setGender("male");
        return user;
    }

    private UserVO createTestUserVO() {
        UserVO vo = new UserVO();
        vo.setId(1L);
        vo.setName("test");
        vo.setAge(20);
        vo.setGender("male");
        return vo;
    }

    private CreateUserRequest createValidCreateRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("test");
        request.setAge(20);
        request.setGender("male");
        return request;
    }

    private UpdateUserRequest createValidUpdateRequest() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1L);
        request.setName("updated");
        request.setAge(25);
        request.setGender("female");
        return request;
    }
}
