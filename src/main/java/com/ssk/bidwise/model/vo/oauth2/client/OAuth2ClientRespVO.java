package com.ssk.bidwise.model.vo.oauth2.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * OAuth2 客户端 Response VO
 */
@Data
@Schema(description = "OAuth2 客户端详情")
public class OAuth2ClientRespVO {

    @Schema(description = "客户端 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "客户端编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo-client")
    private String clientId;

    @Schema(description = "客户端名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "示例客户端")
    private String clientName;

    @Schema(description = "客户端描述", example = "这是一个示例客户端")
    private String description;

    @Schema(description = "重定向 URI", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://example.com/callback")
    private String redirectUris;

    @Schema(description = "授权类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "authorization_code")
    private String authorizedGrantTypes;

    @Schema(description = "授权范围", example = "read write")
    private String scope;

    @Schema(description = "是否自动批准", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Integer autoApprove;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "访问令牌有效期（秒）", example = "1800")
    private Integer accessTokenValiditySeconds;

    @Schema(description = "刷新令牌有效期（秒）", example = "86400")
    private Integer refreshTokenValiditySeconds;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
