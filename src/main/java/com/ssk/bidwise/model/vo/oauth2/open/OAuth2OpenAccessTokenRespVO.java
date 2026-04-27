package com.ssk.bidwise.model.vo.oauth2.open;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * OAuth2 开放接口 - 访问令牌信息 Response VO
 */
@Data
@Schema(description = "访问令牌信息")
public class OAuth2OpenAccessTokenRespVO {

    @Schema(description = "用户 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "客户端 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long clientId;

    @Schema(description = "授权范围", example = "read write")
    private String scope;

    @Schema(description = "过期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long expiresAt;
}
