package com.ssk.bidwise.model.vo.oauth2.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * OAuth2 用户信息 Response VO
 */
@Data
@Schema(description = "用户信息")
public class OAuth2UserInfoRespVO {

    @Schema(description = "用户 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "昵称", example = "张三")
    private String nickname;

    @Schema(description = "头像", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "邮箱", example = "test@example.com")
    private String email;
}
