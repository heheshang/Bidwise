package com.ssk.bidwise.controller.open.oauth2;

import com.ssk.bidwise.common.result.Result;
import com.ssk.bidwise.controller.admin.oauth2.vo.open.OAuth2OpenAccessTokenRespVO;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.ssk.bidwise.model.vo.oauth2.token.OAuth2AccessTokenRespVO;
import com.ssk.bidwise.service.oauth2.OAuth2CodeService;
import com.ssk.bidwise.service.oauth2.OAuth2GrantService;
import com.ssk.bidwise.service.oauth2.OAuth2TokenService;
import com.ssk.bidwise.util.oauth2.OAuth2Helper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ssk.bidwise.common.result.Result.success;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 标准 OAuth2.0 开放端点
 * 遵循 RFC 6749 规范
 *
 * @author Bidwise
 */
@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Tag(name = "OAuth2.0 开放接口", description = "标准 OAuth2 授权端点")
@Validated
public class OAuth2OpenController {

    private final OAuth2GrantService oAuth2GrantService;
    private final OAuth2TokenService oAuth2TokenService;
    private final OAuth2CodeService oAuth2CodeService;

    /**
     * POST /oauth2/token - 获取访问令牌
     * 支持四种授权模式：
     * - authorization_code: 授权码模式
     * - password: 密码模式
     * - client_credentials: 客户端凭证模式
     * - refresh_token: 刷新令牌模式
     *
     * client_id 和 client_secret 通过 HTTP Basic Auth 传递
     */
    @PostMapping("/token")
    @Operation(summary = "获取访问令牌")
    public Result<OAuth2OpenAccessTokenRespVO> getToken(
            HttpServletRequest request,
            @NotBlank(message = "grant_type 不能为空") @RequestParam("grant_type") String grantType,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "redirect_uri", required = false) String redirectUri,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "refresh_token", required = false) String refreshToken,
            @RequestParam(value = "state", required = false) String state) {

        // 从 HTTP Basic Auth 获取 clientId 和 clientSecret
        Long clientId = OAuth2Helper.getClientId(request);
        String clientSecret = OAuth2Helper.getClientSecret(request);

        // TODO: 校验 clientId 和 clientSecret

        OAuth2AccessTokenRespVO result = switch (grantType) {
            case "authorization_code" ->
                oAuth2GrantService.grantByAuthorizationCode(code, clientId, redirectUri);
            case "password" ->
                oAuth2GrantService.grantByPassword(username, password, clientId, scope);
            case "client_credentials" ->
                oAuth2GrantService.grantByClientCredentials(clientId, scope);
            case "refresh_token" ->
                oAuth2GrantService.grantByRefreshToken(refreshToken, clientId);
            default -> throw new IllegalArgumentException("不支持的 grant_type: " + grantType);
        };

        // 转换为 OAuth2 规范格式（字段名下划线）
        OAuth2OpenAccessTokenRespVO resp = convert(result);
        return success(resp);
    }

    /**
     * POST /oauth2/check-token - 校验访问令牌
     */
    @PostMapping("/check-token")
    @Operation(summary = "校验访问令牌")
    public Result<com.ssk.bidwise.model.vo.oauth2.open.OAuth2OpenAccessTokenRespVO> checkToken(
            @NotBlank(message = "token 不能为空") @RequestParam("token") String token) {
        var tokenInfo = oAuth2TokenService.getTokenInfo(token);
        return success(tokenInfo);
    }

    /**
     * DELETE /oauth2/token - 注销令牌
     */
    @DeleteMapping("/token")
    @Operation(summary = "注销访问令牌")
    public Result<Void> revokeToken(
            HttpServletRequest request,
            @NotBlank(message = "token 不能为空") @RequestParam("token") String token) {
        // TODO: 校验客户端
        oAuth2TokenService.removeAccessToken(token);
        return success();
    }

    /**
     * GET /oauth2/authorize - 发起授权（授权码模式）
     * 用户已登录后，客户端跳转到这里授权
     */
    @GetMapping("/authorize")
    @Operation(summary = "发起授权（授权码模式）")
    public Result<?> authorize(
            @NotBlank(message = "response_type 不能为空") @RequestParam("response_type") String responseType,
            @NotBlank(message = "client_id 不能为空") @RequestParam("client_id") Long clientId,
            @NotBlank(message = "redirect_uri 不能为空") @RequestParam("redirect_uri") String redirectUri,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "state", required = false) String state) {

        // 检查用户已登录
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // 用户未登录，返回需要登录
            return Result.error(401, "用户未登录");
        }

        // 获取当前用户 ID
        // TODO: 从 Authentication 提取用户 ID
        Long userId = 1L; // 临时硬编码，需要从 Security 上下文获取

        // 创建授权码
        String code = oAuth2CodeService.createAuthorizationCode(userId, clientId, redirectUri, scope);

        // 重定向到回调地址，带着 code 和 state
        // 这里返回给前端重定向信息
        return success("https://" + redirectUri + "?code=" + code + (state != null ? "&state=" + state : ""));
    }

    /**
     * 转换为 OAuth2 规范响应格式（字段名使用下划线）
     */
    private OAuth2OpenAccessTokenRespVO convert(OAuth2AccessTokenRespVO source) {
        OAuth2OpenAccessTokenRespVO target = new OAuth2OpenAccessTokenRespVO();
        target.setAccess_token(source.getAccessToken());
        target.setRefresh_token(source.getRefreshToken());
        target.setToken_type(source.getTokenType());
        target.setExpires_in(source.getExpiresIn());
        target.setScope(source.getScope());
        return target;
    }
}
