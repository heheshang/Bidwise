package com.ssk.bidwise.service.oauth2;

import com.ssk.bidwise.model.vo.oauth2.open.OAuth2OpenAccessTokenRespVO;
import com.ssk.bidwise.model.vo.oauth2.token.OAuth2AccessTokenRespVO;

/**
 * OAuth2 令牌 Service 接口
 *
 * @author Bidwise
 */
public interface OAuth2TokenService {

    /**
     * 创建访问令牌
     *
     * @param userId 用户 ID
     * @param clientId 客户端 ID
     * @param grantType 授权类型
     * @param scope 授权范围
     * @return 访问令牌响应
     */
    OAuth2AccessTokenRespVO createAccessToken(Long userId, Long clientId, String grantType, String scope);

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @param clientId 客户端 ID
     * @return 访问令牌响应
     */
    OAuth2AccessTokenRespVO refreshAccessToken(String refreshToken, Long clientId);

    /**
     * 移除访问令牌
     *
     * @param accessToken 访问令牌
     */
    void removeAccessToken(String accessToken);

    /**
     * 移除刷新令牌
     *
     * @param refreshToken 刷新令牌
     */
    void removeRefreshToken(String refreshToken);

    /**
     * 校验访问令牌
     *
     * @param accessToken 访问令牌
     * @return 用户 ID，如果无效返回 null
     */
    Long checkAccessToken(String accessToken);

    /**
     * 获得访问令牌信息
     *
     * @param accessToken 访问令牌
     * @return 访问令牌响应
     */
    OAuth2OpenAccessTokenRespVO getTokenInfo(String accessToken);
}
