package com.ssk.bidwise.model.vo.oauth2.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * OAuth2 客户端分页 Request VO
 */
@Data
@Schema(description = "OAuth2 客户端分页查询")
public class OAuth2ClientPageReqVO {

    @Schema(description = "客户端名称，模糊匹配", example = "示例")
    private String name;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer page = 1;

    @Schema(description = "每页大小", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer size = 10;

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }
}
