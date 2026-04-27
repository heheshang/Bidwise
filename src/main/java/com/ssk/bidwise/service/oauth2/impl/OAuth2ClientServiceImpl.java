package com.ssk.bidwise.service.oauth2.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssk.bidwise.common.exception.BusinessException;
import com.ssk.bidwise.common.exception.ErrorCode;
import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.converter.oauth2.OAuth2ClientConverter;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2ClientDO;
import com.ssk.bidwise.dal.mysql.oauth2.OAuth2ClientMapper;
import com.ssk.bidwise.dal.redis.oauth2.OAuth2ClientRedisCache;
import com.ssk.bidwise.model.vo.oauth2.client.OAuth2ClientPageReqVO;
import com.ssk.bidwise.model.vo.oauth2.client.OAuth2ClientRespVO;
import com.ssk.bidwise.model.vo.oauth2.client.OAuth2ClientSaveReqVO;
import com.ssk.bidwise.service.oauth2.OAuth2ClientService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * OAuth2 客户端 Service 实现
 *
 * @author Bidwise
 */
@Service
@RequiredArgsConstructor
public class OAuth2ClientServiceImpl implements OAuth2ClientService {

    private final OAuth2ClientMapper oAuth2ClientMapper;
    private final OAuth2ClientConverter oAuth2ClientConverter;
    private final OAuth2ClientRedisCache oAuth2ClientRedisCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createClient(OAuth2ClientSaveReqVO reqVO) {
        OAuth2ClientDO client = oAuth2ClientConverter.convert(reqVO);
        oAuth2ClientMapper.insert(client);
        return client.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateClient(OAuth2ClientSaveReqVO reqVO) {
        if (reqVO.getId() == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "客户端 ID 不能为空");
        }
        OAuth2ClientDO existing = oAuth2ClientMapper.selectById(reqVO.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_EXIST, "OAuth2 客户端不存在");
        }
        OAuth2ClientDO client = oAuth2ClientConverter.convert(reqVO);
        client.setId(reqVO.getId());
        oAuth2ClientMapper.updateById(client);
        oAuth2ClientRedisCache.remove(existing.getClientId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClient(Long id) {
        OAuth2ClientDO client = oAuth2ClientMapper.selectById(id);
        if (client == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_EXIST, "OAuth2 客户端不存在");
        }
        oAuth2ClientMapper.deleteById(id);
        oAuth2ClientRedisCache.remove(client.getClientId());
    }

    @Override
    public PageVO<OAuth2ClientRespVO> getClientPage(OAuth2ClientPageReqVO pageReqVO) {
        Page<OAuth2ClientDO> page = new Page<>(pageReqVO.getPage(), pageReqVO.getSize());
        LambdaQueryWrapper<OAuth2ClientDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(pageReqVO.getName())) {
            wrapper.like(OAuth2ClientDO::getClientName, pageReqVO.getName());
        }
        if (pageReqVO.getStatus() != null) {
            wrapper.eq(OAuth2ClientDO::getStatus, pageReqVO.getStatus());
        }
        wrapper.orderByDesc(OAuth2ClientDO::getCreateTime);
        Page<OAuth2ClientDO> result = oAuth2ClientMapper.selectPage(page, wrapper);

        PageVO<OAuth2ClientRespVO> pageVO = new PageVO<>();
        pageVO.setPage(pageReqVO.getPage());
        pageVO.setSize(pageReqVO.getSize());
        pageVO.setTotal(result.getTotal());
        pageVO.setList(result.getRecords().stream()
                .map(oAuth2ClientConverter::convert)
                .toList());
        return pageVO;
    }

    @Override
    public OAuth2ClientRespVO getClientDetail(Long id) {
        OAuth2ClientDO client = oAuth2ClientMapper.selectById(id);
        if (client == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_EXIST, "OAuth2 客户端不存在");
        }
        return oAuth2ClientConverter.convert(client);
    }

    @Override
    public OAuth2ClientDO getClientByClientId(String clientId) {
        OAuth2ClientDO cached = oAuth2ClientRedisCache.get(clientId);
        if (cached != null) {
            return cached;
        }
        LambdaQueryWrapper<OAuth2ClientDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OAuth2ClientDO::getClientId, clientId);
        OAuth2ClientDO client = oAuth2ClientMapper.selectOne(wrapper);
        if (client != null) {
            oAuth2ClientRedisCache.save(client, 3600);
        }
        return client;
    }

    @Override
    public OAuth2ClientDO getClientDOById(Long id) {
        return oAuth2ClientMapper.selectById(id);
    }
}
