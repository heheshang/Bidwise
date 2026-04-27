package com.ssk.bidwise.service.oauth2.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ssk.bidwise.common.exception.BusinessException;
import com.ssk.bidwise.common.exception.ErrorCode;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2CodeDO;
import com.ssk.bidwise.dal.postgres.oauth2.OAuth2CodeMapper;
import com.ssk.bidwise.service.oauth2.OAuth2CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * OAuth2 授权码 Service 实现
 *
 * @author Bidwise
 */
@Service
@RequiredArgsConstructor
public class OAuth2CodeServiceImpl implements OAuth2CodeService {

    private final OAuth2CodeMapper oAuth2CodeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createAuthorizationCode(Long userId, Long clientId, String redirectUri, String scope) {
        // 生成随机授权码
        String code = UUID.randomUUID().toString().replace("-", "");

        // 授权码有效期 5 分钟
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);

        OAuth2CodeDO codeDO = new OAuth2CodeDO();
        codeDO.setUserId(userId);
        codeDO.setClientId(clientId);
        codeDO.setCode(code);
        codeDO.setRedirectUri(redirectUri);
        codeDO.setScope(scope);
        codeDO.setExpirationTime(expirationTime);

        oAuth2CodeMapper.insert(codeDO);
        return code;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuth2CodeDO consumeAuthorizationCode(String code, Long clientId) {
        // 查询授权码
        LambdaQueryWrapper<OAuth2CodeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OAuth2CodeDO::getCode, code)
                .eq(OAuth2CodeDO::getClientId, clientId);
        OAuth2CodeDO codeDO = oAuth2CodeMapper.selectOne(wrapper);

        if (codeDO == null) {
            throw new BusinessException(ErrorCode.OAUTH2_CODE_NOT_EXIST, "授权码不存在");
        }

        // 检查是否过期
        if (codeDO.getExpirationTime().isBefore(LocalDateTime.now())) {
            oAuth2CodeMapper.deleteById(codeDO.getId());
            throw new BusinessException(ErrorCode.OAUTH2_CODE_EXPIRED, "授权码已过期");
        }

        // 使用后删除（一次性）
        oAuth2CodeMapper.deleteById(codeDO.getId());

        return codeDO;
    }
}
