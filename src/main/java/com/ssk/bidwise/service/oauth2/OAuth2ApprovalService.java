package com.ssk.bidwise.service.oauth2;

/**
 * OAuth2 授权批准 Service 接口
 * 记录用户对客户端的授权批准
 *
 * @author Bidwise
 */
public interface OAuth2ApprovalService {

    /**
     * 批准或拒绝客户端授权
     *
     * @param userId 用户 ID
     * @param clientId 客户端 ID
     * @param scope 授权范围
     * @param approved 是否批准
     * @return 是否成功
     */
    boolean approve(Long userId, Long clientId, String scope, boolean approved);

    /**
     * 检查用户是否已经批准客户端的指定范围
     *
     * @param userId 用户 ID
     * @param clientId 客户端 ID
     * @param scope 授权范围
     * @return 是否批准
     */
    boolean checkApproved(Long userId, Long clientId, String scope);
}
