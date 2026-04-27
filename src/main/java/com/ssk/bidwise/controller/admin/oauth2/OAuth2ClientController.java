package com.ssk.bidwise.controller.admin.oauth2;

import com.ssk.bidwise.common.result.Result;
import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.model.vo.oauth2.client.OAuth2ClientPageReqVO;
import com.ssk.bidwise.model.vo.oauth2.client.OAuth2ClientRespVO;
import com.ssk.bidwise.model.vo.oauth2.client.OAuth2ClientSaveReqVO;
import com.ssk.bidwise.service.oauth2.OAuth2ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAuth2 客户端管理 Controller
 *
 * @author Bidwise
 */
@Tag(name = "管理后台 - OAuth2 客户端")
@RestController
@RequestMapping("/api/v1/oauth2/client")
@RequiredArgsConstructor
public class OAuth2ClientController {

    private final OAuth2ClientService oAuth2ClientService;

    @Operation(summary = "创建客户端")
    @PostMapping
    public Result<Long> createClient(@Valid @RequestBody OAuth2ClientSaveReqVO reqVO) {
        Long id = oAuth2ClientService.createClient(reqVO);
        return Result.success(id);
    }

    @Operation(summary = "更新客户端")
    @PutMapping
    public Result<Void> updateClient(@Valid @RequestBody OAuth2ClientSaveReqVO reqVO) {
        oAuth2ClientService.updateClient(reqVO);
        return Result.success();
    }

    @Operation(summary = "删除客户端")
    @DeleteMapping("/{id}")
    @Parameter(name = "id", description = "编号", required = true)
    public Result<Void> deleteClient(@PathVariable Long id) {
        oAuth2ClientService.deleteClient(id);
        return Result.success();
    }

    @Operation(summary = "获取客户端分页")
    @GetMapping("/page")
    public Result<PageVO<OAuth2ClientRespVO>> getClientPage(OAuth2ClientPageReqVO pageReqVO) {
        PageVO<OAuth2ClientRespVO> page = oAuth2ClientService.getClientPage(pageReqVO);
        return Result.success(page);
    }

    @Operation(summary = "获取客户端详情")
    @GetMapping("/{id}")
    @Parameter(name = "id", description = "编号", required = true)
    public Result<OAuth2ClientRespVO> getClientDetail(@PathVariable Long id) {
        OAuth2ClientRespVO client = oAuth2ClientService.getClientDetail(id);
        return Result.success(client);
    }
}
