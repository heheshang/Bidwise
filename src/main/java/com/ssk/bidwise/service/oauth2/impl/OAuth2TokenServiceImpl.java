package com.ssk.bidwise.service.oauth2.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ssk.bidwise.common.exception.BusinessException;
import com.ssk.bidwise.common.exception.ErrorCode;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2ClientDO;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2RefreshTokenDO;
import com.ssk.bidwise.dal.postgres.oauth2.OAuth2AccessTokenMapper;
import com.ssk.bidwise.dal.postgres.oauth2.OAuth2RefreshTokenMapper;
import com.ssk.bidwise.dal.redis.oauth2.OAuth2AccessTokenRedisCache;
import com.ssk.bidwise.dal.redis.oauth2.OAuth2RefreshTokenRedisCache;
import com.ssk.bidwise.model.vo.oauth2.open.OAuth2OpenAccessTokenRespVO;
import com.ssk.bidwise.model.vo.oauth2.token.OAuth2AccessTokenRespVO;
import com.ssk.bidwise.service.oauth2.OAuth2ClientService;
import com.ssk.bidwise.service.oauth2.OAuth2TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * OAuth2 令牌 Service 实现
 *
 * @author Bidwise
 */
@Service
@RequiredArgsConstructor
public class OAuth2TokenServiceImpl implements OAuth2TokenService {

    private final OAuth2AccessTokenMapper oAuth2AccessTokenMapper;
    private final OAuth2RefreshTokenMapper oAuth2RefreshTokenMapper;
    private final OAuth2AccessTokenRedisCache oAuth2AccessTokenRedisCache;
    private final OAuth2RefreshTokenRedisCache oAuth2RefreshTokenRedisCache;
    private final OAuth2ClientService oAuth2ClientService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuth2AccessTokenRespVO createAccessToken(Long userId, Long clientId, String grantType, String scope) {
        OAuth2ClientDO client = oAuth2ClientService.getClientDOById(clientId);
        if (client == null) {
            throw new BusinessException(ErrorCode.OAUTH2_CLIENT_NOT_EXIST, "OAuth2 客户端不存在");
        }

        // 生成访问令牌和刷新令牌
        String accessToken = UUID.randomUUID().toString().replace("-", "");
        String refreshToken = UUID.randomUUID().toString().replace("-", "");

        // 计算过期时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime accessTokenExpires = now.plusSeconds(client.getAccessTokenValiditySeconds());
        LocalDateTime refreshTokenExpires = now.plusSeconds(client.getRefreshTokenValiditySeconds());

        // 保存访问令牌
        OAuth2AccessTokenDO accessTokenDO = new OAuth2AccessTokenDO();
        accessTokenDO.setUserId(userId);
        accessTokenDO.setClientId(clientId);
        accessTokenDO.setAccessToken(accessToken);
        accessTokenDO.setExpirationTime(accessTokenExpires);
        oAuth2AccessTokenMapper.insert(accessTokenDO);

        // 保存刷新令牌
        OAuth2RefreshTokenDO refreshTokenDO = new OAuth2RefreshTokenDO();
        refreshTokenDO.setUserId(userId);
        refreshTokenDO.setClientId(clientId);
        refreshTokenDO.setRefreshToken(refreshToken);
        refreshTokenDO.setAccessTokenId(accessTokenDO.getId());
        refreshTokenDO.setExpirationTime(refreshTokenExpires);
        oAuth2RefreshTokenMapper.insert(refreshTokenDO);

        // 缓存到 Redis
        long expireSeconds = client.getAccessTokenValiditySeconds();
        oAuth2AccessTokenRedisCache.save(accessTokenDO, expireSeconds);

        // 返回结果
        return new OAuth2AccessTokenRespVO(
                accessToken,
                refreshToken,
                client.getAccessTokenValiditySeconds().longValue(),
                "Bearer",
                userId,
                scope
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuth2AccessTokenRespVO refreshAccessToken(String refreshToken, Long clientId) {
        // 查询刷新令牌
        LambdaQueryWrapper<OAuth2RefreshTokenDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OAuth2RefreshTokenDO::getRefreshToken, refreshToken);
        OAuth2RefreshTokenDO refreshTokenDO = oAuth2RefreshTokenMapper.selectOne(wrapper);

        if (refreshTokenDO == null) {
            throw new BusinessException(ErrorCode.OAUTH2_REFRESH_TOKEN_NOT_EXIST, "刷新令牌不存在");
        }

        // 检查客户端 ID 匹配
        if (!refreshTokenDO.getClientId().equals(clientId)) {
            throw new BusinessException(ErrorCode.OAUTH2_REFRESH_TOKEN_INVALID, "刷新令牌不匹配当前客户端");
        }

        // 检查是否过期
        if (refreshTokenDO.getExpirationTime().isBefore(LocalDateTime.now())) {
            oAuth2RefreshTokenMapper.deleteById(refreshTokenDO.getId());
            throw new BusinessException(ErrorCode.OAUTH2_REFRESH_TOKEN_EXPIRED, "刷新令牌已过期");
        }

        OAuth2ClientDO client = oAuth2ClientService.getClientDOById(clientId);
        Long userId = refreshTokenDO.getUserId();

        // 删除旧的访问令牌
        if (refreshTokenDO.getAccessTokenId() != null) {
            oAuth2AccessTokenMapper.deleteById(refreshTokenDO.getAccessTokenId());
        }

        // 创建新的访问令牌
        return createAccessToken(userId, clientId, "refresh_token", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAccessToken(String accessToken) {
        // 查询访问令牌
        LambdaQueryWrapper<OAuth2AccessTokenDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OAuth2AccessTokenDO::getAccessToken, accessToken);
        OAuth2AccessTokenDO accessTokenDO = oAuth2AccessTokenMapper.selectOne(wrapper);

        if (accessTokenDO != null) {
            // 删除 PostgreSQL 记录
            oAuth2AccessTokenMapper.deleteById(accessTokenDO.getId());
            // 删除 Redis 缓存
            oAuth2AccessTokenRedisCache.remove(accessToken);

            // 删除关联的刷新令牌
            LambdaQueryWrapper<OAuth2RefreshTokenDO> refreshWrapper = new LambdaQueryWrapper<>();
            refreshWrapper.eq(OAuth2RefreshTokenDO::getAccessTokenId, accessTokenDO.getId());
            OAuth2RefreshTokenDO refreshTokenDO = oAuth2RefreshTokenMapper.selectOne(refreshWrapper);
            if (refreshTokenDO != null) {
                oAuth2RefreshTokenMapper.deleteById(refreshTokenDO.getId());
                oAuth2RefreshTokenRedisCache.remove(refreshTokenDO.getRefreshToken());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRefreshToken(String refreshToken) {
        LambdaQueryWrapper<OAuth2RefreshTokenDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OAuth2RefreshTokenDO::getRefreshToken, refreshToken);
        OAuth2RefreshTokenDO refreshTokenDO = oAuth2RefreshTokenMapper.selectOne(wrapper);

        if (refreshTokenDO != null) {
            oAuth2RefreshTokenMapper.deleteById(refreshTokenDO.getId());
            oAuth2RefreshTokenRedisCache.remove(refreshToken);

            // 删除关联的访问令牌
            if (refreshTokenDO.getAccessTokenId() != null) {
                oAuth2AccessTokenMapper.deleteById(refreshTokenDO.getAccessTokenId());
            }
        }
    }

    @Override
    public Long checkAccessToken(String accessToken) {
        // 先从 Redis 获取
        OAuth2AccessTokenDO cached = oAuth2AccessTokenRedisCache.get(accessToken);
        if (cached != null) {
            // 检查是否过期
            if (cached.getExpirationTime().isAfter(LocalDateTime.now())) {
                return cached.getUserId();
            }
            // 过期删除
            oAuth2AccessTokenRedisCache.remove(accessToken);
            return null;
        }

        // Redis 未命中，查 PostgreSQL
        LambdaQueryWrapper<OAuth2AccessTokenDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OAuth2AccessTokenDO::getAccessToken, accessToken);
        OAuth2AccessTokenDO accessTokenDO = oAuth2AccessTokenMapper.selectOne(wrapper);

        if (accessTokenDO == null) {
            return null;
        }

        // 检查是否过期
        if (accessTokenDO.getExpirationTime().isBefore(LocalDateTime.now())) {
            oAuth2AccessTokenMapper.deleteById(accessTokenDO.getId());
            return null;
        }

        return accessTokenDO.getUserId();
    }

    @Override
    public OAuth2OpenAccessTokenRespVO getTokenInfo(String accessToken) {
        // 查询访问令牌
        LambdaQueryWrapper<OAuth2AccessTokenDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OAuth2AccessTokenDO::getAccessToken, accessToken);
        OAuth2AccessTokenDO accessTokenDO = oAuth2AccessTokenMapper.selectOne(wrapper);

        if (accessTokenDO == null || accessTokenDO.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.OAUTH2_ACCESS_TOKEN_INVALID, "访问令牌无效或已过期");
        }

        OAuth2OpenAccessTokenRespVO respVO = new OAuth2OpenAccessTokenRespVO();
        respVO.setUserId(accessTokenDO.getUserId());
        respVO.setClientId(accessTokenDO.getClientId());
        respVO.setExpiresAt(accessTokenDO.getExpirationTime().toEpochSecond(ZoneOffset.UTC));
        return respVO;
    }
}
