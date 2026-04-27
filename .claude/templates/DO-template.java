// .claude/templates/DO-template.java
package com.example.auth.dal.dataobject.oauth2;

import com.example.auth.framework.mybatis.core.dataobject.AuditBaseDO;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * OAuth2 ${实体名} DO
 */
@TableName(value = "sys_oauth2_${表名}", autoResultMap = true)
@IdSequence("sys_oauth2_${表名}_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class OAuth2${实体名}DO extends AuditBaseDO {

    @TableId
    private Long id;

    // --- 业务字段（根据实际需求填充）---

    // JSON 数组字段示例
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> scopes;

    // 过期时间字段（如有）
    private LocalDateTime expiresTime;
}
