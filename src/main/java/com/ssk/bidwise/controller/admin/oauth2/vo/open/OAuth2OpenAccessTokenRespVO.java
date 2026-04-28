package com.ssk.bidwise.controller.admin.oauth2.vo.open;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * OAuth2 开放接口 - 访问令牌响应 VO
 * 遵循 RFC 6749 规范，字段名使用下划线
 *
 * @author Bidwise
 */
@Data
@Schema(description = "OAuth2 访问令牌响应")
public class OAuth2OpenAccessTokenRespVO {

    @Schema(description = "访问令牌", requiredMode = Schema.RequiredMode.REQUIRED)
    private String access_token;

    @Schema(description = "刷新令牌", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refresh_token;

    @Schema(description = "令牌类型，通常是 Bearer", requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "Bearer")
    private String token_type = "Bearer";

    @Schema(description = "过期时间，单位秒", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long expires_in;

    @Schema(description = "授权范围")
    private String scope;
}
