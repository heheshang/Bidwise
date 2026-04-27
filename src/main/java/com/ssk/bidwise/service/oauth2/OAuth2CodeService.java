package com.ssk.bidwise.service.oauth2;

import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2CodeDO;

/**
 * OAuth2 授权码 Service 接口
 *
 * @author Bidwise
 */
public interface OAuth2CodeService {

    /**
     * 创建授权码
     *
     * @param userId 用户 ID
     * @param clientId 客户端 ID
     * @param redirectUri 重定向 URI
     * @param scope 授权范围
     * @return 授权码
     */
    String createAuthorizationCode(Long userId, Long clientId, String redirectUri, String scope);

    /**
     * 使用授权码，使用后移除
     *
     * @param code 授权码
     * @param clientId 客户端 ID
     * @return 授权码信息
     */
    OAuth2CodeDO consumeAuthorizationCode(String code, Long clientId);
}
