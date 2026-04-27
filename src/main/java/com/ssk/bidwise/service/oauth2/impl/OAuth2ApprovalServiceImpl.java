package com.ssk.bidwise.service.oauth2.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2ApprovalDO;
import com.ssk.bidwise.dal.postgres.oauth2.OAuth2ApprovalMapper;
import com.ssk.bidwise.service.oauth2.OAuth2ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * OAuth2 授权批准 Service 实现
 *
 * @author Bidwise
 */
@Service
@RequiredArgsConstructor
public class OAuth2ApprovalServiceImpl implements OAuth2ApprovalService {

    private final OAuth2ApprovalMapper oAuth2ApprovalMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approve(Long userId, Long clientId, String scope, boolean approved) {
        // 查询是否已有授权记录
        LambdaQueryWrapper<OAuth2ApprovalDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OAuth2ApprovalDO::getUserId, userId)
                .eq(OAuth2ApprovalDO::getClientId, clientId)
                .eq(OAuth2ApprovalDO::getScope, scope);
        OAuth2ApprovalDO existing = oAuth2ApprovalMapper.selectOne(wrapper);

        // 设置过期时间为 30 天
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(30);
        Integer status = approved ? 1 : 0;

        if (existing != null) {
            // 更新现有记录
            existing.setStatus(status);
            existing.setExpiresAt(expiresAt);
            oAuth2ApprovalMapper.updateById(existing);
        } else {
            // 新建记录
            OAuth2ApprovalDO approval = new OAuth2ApprovalDO();
            approval.setUserId(userId);
            approval.setClientId(clientId);
            approval.setScope(scope);
            approval.setStatus(status);
            approval.setExpiresAt(expiresAt);
            oAuth2ApprovalMapper.insert(approval);
        }

        return true;
    }

    @Override
    public boolean checkApproved(Long userId, Long clientId, String scope) {
        LambdaQueryWrapper<OAuth2ApprovalDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OAuth2ApprovalDO::getUserId, userId)
                .eq(OAuth2ApprovalDO::getClientId, clientId)
                .eq(OAuth2ApprovalDO::getScope, scope)
                .eq(OAuth2ApprovalDO::getStatus, 1) // 已批准
                .gt(OAuth2ApprovalDO::getExpiresAt, LocalDateTime.now()); // 未过期
        return oAuth2ApprovalMapper.selectCount(wrapper) > 0;
    }
}
