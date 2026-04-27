package com.ssk.bidwise.controller.admin.oauth2;

import com.ssk.bidwise.common.result.Result;
import com.ssk.bidwise.model.vo.oauth2.open.OAuth2OpenAccessTokenRespVO;
import com.ssk.bidwise.model.vo.oauth2.token.OAuth2AccessTokenRespVO;
import com.ssk.bidwise.service.oauth2.OAuth2TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAuth2 令牌管理 Controller
 *
 * @author Bidwise
 */
@Tag(name = "管理后台 - OAuth2 令牌")
@RestController
@RequestMapping("/api/v1/oauth2/token")
@RequiredArgsConstructor
public class OAuth2TokenController {

    private final OAuth2TokenService oAuth2TokenService;

    @Operation(summary = "获取令牌信息")
    @GetMapping("/info")
    @Parameter(name = "accessToken", description = "访问令牌", required = true)
    public Result<OAuth2OpenAccessTokenRespVO> getTokenInfo(String accessToken) {
        OAuth2OpenAccessTokenRespVO tokenInfo = oAuth2TokenService.getTokenInfo(accessToken);
        return Result.success(tokenInfo);
    }

    @Operation(summary = "移除访问令牌")
    @DeleteMapping("/access-token")
    @Parameter(name = "accessToken", description = "访问令牌", required = true)
    public Result<Void> removeAccessToken(String accessToken) {
        oAuth2TokenService.removeAccessToken(accessToken);
        return Result.success();
    }

    @Operation(summary = "移除刷新令牌")
    @DeleteMapping("/refresh-token")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    public Result<Void> removeRefreshToken(String refreshToken) {
        oAuth2TokenService.removeRefreshToken(refreshToken);
        return Result.success();
    }
}
