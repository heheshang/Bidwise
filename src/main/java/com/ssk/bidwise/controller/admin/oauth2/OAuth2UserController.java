package com.ssk.bidwise.controller.admin.oauth2;

import com.ssk.bidwise.common.result.Result;
import com.ssk.bidwise.model.vo.oauth2.user.OAuth2UserInfoRespVO;
import com.ssk.bidwise.service.oauth2.OAuth2TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAuth2 用户信息 Controller
 *
 * @author Bidwise
 */
@Tag(name = "OAuth2 用户信息")
@RestController
@RequestMapping("/api/v1/oauth2/user")
@RequiredArgsConstructor
public class OAuth2UserController {

    private final OAuth2TokenService oAuth2TokenService;

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public Result<OAuth2UserInfoRespVO> getCurrentUserInfo() {
        // TODO: 从 Token 获取用户信息，返回用户信息
        return Result.success(null);
    }
}
