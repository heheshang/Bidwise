package com.ssk.bidwise.service.oauth2.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ssk.bidwise.common.exception.BusinessException;
import com.ssk.bidwise.common.exception.ErrorCode;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2ClientDO;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2CodeDO;
import com.ssk.bidwise.dal.dataobject.system.UserDO;
import com.ssk.bidwise.dal.postgres.system.UserMapper;
import com.ssk.bidwise.model.vo.oauth2.token.OAuth2AccessTokenRespVO;
import com.ssk.bidwise.service.oauth2.OAuth2ApprovalService;
import com.ssk.bidwise.service.oauth2.OAuth2ClientService;
import com.ssk.bidwise.service.oauth2.OAuth2CodeService;
import com.ssk.bidwise.service.oauth2.OAuth2GrantService;
import com.ssk.bidwise.service.oauth2.OAuth2TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * OAuth2 授权 Service 实现
 * 支持四种授权模式
 *
 * @author Bidwise
 */
@Service
@RequiredArgsConstructor
public class OAuth2GrantServiceImpl implements OAuth2GrantService {

    private final OAuth2ClientService oAuth2ClientService;
    private final OAuth2CodeService oAuth2CodeService;
    private final OAuth2TokenService oAuth2TokenService;
    private final OAuth2ApprovalService oAuth2ApprovalService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2AccessTokenRespVO grantByAuthorizationCode(String code, Long clientId, String redirectUri) {
        // 验证客户端
        OAuth2ClientDO client = validateClient(clientId);

        // 消费授权码（验证并删除）
        OAuth2CodeDO codeDO = oAuth2CodeService.consumeAuthorizationCode(code, clientId);

        // 验证重定向 URI
        if (!redirectUri.equals(codeDO.getRedirectUri())) {
            throw new BusinessException(ErrorCode.OAUTH2_REDIRECT_URI_MISMATCH, "重定向 URI 不匹配");
        }

        // 创建访问令牌
        return oAuth2TokenService.createAccessToken(codeDO.getUserId(), clientId, "authorization_code", codeDO.getScope());
    }

    @Override
    public OAuth2AccessTokenRespVO grantByPassword(String username, String password, Long clientId, String scope) {
        // 验证客户端
        OAuth2ClientDO client = validateClient(clientId);

        // 验证密码模式是否支持
        if (!client.getAuthorizedGrantTypes().contains("password")) {
            throw new BusinessException(ErrorCode.OAUTH2_GRANT_TYPE_NOT_SUPPORTED, "客户端不支持密码模式");
        }

        // 查询用户
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUsername, username);
        UserDO user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST, "用户名或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR, "用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(ErrorCode.USER_DISABLED, "用户已被禁用");
        }

        // 如果需要用户批准，检查是否已批准
        if (client.getAutoApprove() != 1 && StringUtils.hasText(scope)) {
            // 逐个检查 scope 是否批准
            String[] scopes = scope.split(" ");
            for (String s : scopes) {
                if (!oAuth2ApprovalService.checkApproved(user.getId(), clientId, s)) {
                    throw new BusinessException(ErrorCode.OAUTH2_SCOPE_NOT_APPROVED, "范围 " + s + " 未获得用户批准");
                }
            }
        }

        // 创建访问令牌
        return oAuth2TokenService.createAccessToken(user.getId(), clientId, "password", scope);
    }

    @Override
    public OAuth2AccessTokenRespVO grantByClientCredentials(Long clientId, String scope) {
        // 验证客户端
        OAuth2ClientDO client = validateClient(clientId);

        // 验证客户端凭证模式是否支持
        if (!client.getAuthorizedGrantTypes().contains("client_credentials")) {
            throw new BusinessException(ErrorCode.OAUTH2_GRANT_TYPE_NOT_SUPPORTED, "客户端不支持客户端凭证模式");
        }

        // 客户端凭证模式使用客户端自身作为"用户"
        // 创建访问令牌（userId = clientId，这是特殊约定）
        return oAuth2TokenService.createAccessToken(clientId, clientId, "client_credentials", scope);
    }

    @Override
    public OAuth2AccessTokenRespVO grantByRefreshToken(String refreshToken, Long clientId) {
        // 验证客户端
        validateClient(clientId);

        // 刷新访问令牌
        return oAuth2TokenService.refreshAccessToken(refreshToken, clientId);
    }

    /**
     * 验证客户端是否有效
     */
    private OAuth2ClientDO validateClient(Long clientId) {
        OAuth2ClientDO client = oAuth2ClientService.getClientDOById(clientId);
        if (client == null) {
            throw new BusinessException(ErrorCode.OAUTH2_CLIENT_NOT_EXIST, "OAuth2 客户端不存在");
        }
        if (client.getStatus() != 1) {
            throw new BusinessException(ErrorCode.OAUTH2_CLIENT_DISABLED, "OAuth2 客户端已被禁用");
        }
        return client;
    }
}
