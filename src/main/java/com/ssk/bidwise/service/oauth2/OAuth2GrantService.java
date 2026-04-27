package com.ssk.bidwise.service.oauth2;

import com.ssk.bidwise.model.vo.oauth2.token.OAuth2AccessTokenRespVO;

/**
 * OAuth2 授权 Service 接口
 * 支持四种授权模式
 *
 * @author Bidwise
 */
public interface OAuth2GrantService {

    /**
     * 授权码模式 - 授予访问令牌
     *
     * @param code 授权码
     * @param clientId 客户端 ID
     * @param redirectUri 重定向 URI
     * @return 访问令牌响应
     */
    OAuth2AccessTokenRespVO grantByAuthorizationCode(String code, Long clientId, String redirectUri);

    /**
     * 密码模式 - 授予访问令牌
     *
     * @param username 用户名
     * @param password 密码
     * @param clientId 客户端 ID
     * @param scope 授权范围
     * @return 访问令牌响应
     */
    OAuth2AccessTokenRespVO grantByPassword(String username, String password, Long clientId, String scope);

    /**
     * 客户端凭证模式 - 授予访问令牌
     *
     * @param clientId 客户端 ID
     * @param scope 授权范围
     * @return 访问令牌响应
     */
    OAuth2AccessTokenRespVO grantByClientCredentials(Long clientId, String scope);

    /**
     * 刷新令牌模式 - 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @param clientId 客户端 ID
     * @return 访问令牌响应
     */
    OAuth2AccessTokenRespVO grantByRefreshToken(String refreshToken, Long clientId);
}
