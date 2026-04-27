package com.ssk.bidwise.model.vo.oauth2.token;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OAuth2 访问令牌 Response VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "OAuth2 访问令牌")
public class OAuth2AccessTokenRespVO {

    @Schema(description = "访问令牌", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accessToken;

    @Schema(description = "刷新令牌", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;

    @Schema(description = "过期时间，单位：秒", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long expiresIn;

    @Schema(description = "令牌类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "用户 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "授权范围", example = "read write")
    private String scope;
}
