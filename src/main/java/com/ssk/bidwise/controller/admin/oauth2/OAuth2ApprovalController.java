package com.ssk.bidwise.controller.admin.oauth2;

import com.ssk.bidwise.common.result.Result;
import com.ssk.bidwise.service.oauth2.OAuth2ApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAuth2 授权批准 Controller
 *
 * @author Bidwise
 */
@Tag(name = "OAuth2 授权批准")
@RestController
@RequestMapping("/api/v1/oauth2/approval")
@RequiredArgsConstructor
public class OAuth2ApprovalController {

    private final OAuth2ApprovalService oAuth2ApprovalService;

    @Operation(summary = "批准或拒绝授权")
    @PostMapping
    @Parameter(name = "clientId", description = "客户端 ID", required = true)
    @Parameter(name = "scope", description = "授权范围", required = true)
    @Parameter(name = "approved", description = "是否批准", required = true)
    public Result<Boolean> approve(Long userId, Long clientId, String scope, boolean approved) {
        boolean result = oAuth2ApprovalService.approve(userId, clientId, scope, approved);
        return Result.success(result);
    }
}
