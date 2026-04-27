package com.ssk.bidwise.model.vo.oauth2.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * OAuth2 客户端创建/更新 Request VO
 */
@Data
@Schema(description = "OAuth2 客户端创建/更新")
public class OAuth2ClientSaveReqVO {

    @Schema(description = "客户端编号", example = "demo-client")
    private Long id;

    @NotBlank(message = "客户端编号不能为空")
    @Size(max = 100, message = "客户端编号长度不能超过 100 个字符")
    @Schema(description = "客户端编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo-client")
    private String clientId;

    @NotBlank(message = "客户端密钥不能为空")
    @Size(max = 255, message = "客户端密钥长度不能超过 255 个字符")
    @Schema(description = "客户端密钥", requiredMode = Schema.RequiredMode.REQUIRED, example = "secret")
    private String clientSecret;

    @NotBlank(message = "客户端名称不能为空")
    @Size(max = 200, message = "客户端名称长度不能超过 200 个字符")
    @Schema(description = "客户端名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "示例客户端")
    private String clientName;

    @Size(max = 500, message = "客户端描述长度不能超过 500 个字符")
    @Schema(description = "客户端描述", example = "这是一个示例客户端")
    private String description;

    @NotBlank(message = "重定向 URI 不能为空")
    @Schema(description = "重定向 URI，多个用逗号分隔", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://example.com/callback")
    private String redirectUris;

    @NotBlank(message = "授权类型不能为空")
    @Schema(description = "授权类型，多个用逗号分隔", requiredMode = Schema.RequiredMode.REQUIRED, example = "authorization_code,password,client_credentials,refresh_token")
    private String authorizedGrantTypes;

    @Schema(description = "授权范围", example = "read write")
    private String scope;

    @NotNull(message = "是否自动批准不能为空")
    @Schema(description = "是否自动批准", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Integer autoApprove;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "访问令牌有效期（秒），默认 1800", example = "1800")
    private Integer accessTokenValiditySeconds;

    @Schema(description = "刷新令牌有效期（秒），默认 86400", example = "86400")
    private Integer refreshTokenValiditySeconds;
}
