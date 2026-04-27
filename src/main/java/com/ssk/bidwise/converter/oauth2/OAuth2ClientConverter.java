package com.ssk.bidwise.converter.oauth2;

import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2ClientDO;
import com.ssk.bidwise.model.vo.oauth2.client.OAuth2ClientRespVO;
import com.ssk.bidwise.model.vo.oauth2.client.OAuth2ClientSaveReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * OAuth2 客户端 Converter
 */
@Mapper
public interface OAuth2ClientConverter {

    OAuth2ClientConverter INSTANCE = Mappers.getMapper(OAuth2ClientConverter.class);

    /**
     * 转换 Request 为 DO
     */
    OAuth2ClientDO convert(OAuth2ClientSaveReqVO reqVO);

    /**
     * 转换 DO 为 VO
     */
    OAuth2ClientRespVO convert(OAuth2ClientDO clientDO);
}
