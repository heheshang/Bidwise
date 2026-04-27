package com.ssk.bidwise.service.oauth2;

import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2ClientDO;
import com.ssk.bidwise.model.vo.oauth2.client.OAuth2ClientRespVO;
import com.ssk.bidwise.model.vo.oauth2.client.OAuth2ClientPageReqVO;
import com.ssk.bidwise.model.vo.oauth2.client.OAuth2ClientSaveReqVO;

/**
 * OAuth2 客户端 Service 接口
 *
 * @author Bidwise
 */
public interface OAuth2ClientService {

    /**
     * 创建 OAuth2 客户端
     *
     * @param reqVO 创建请求
     * @return 客户端 ID
     */
    Long createClient(OAuth2ClientSaveReqVO reqVO);

    /**
     * 更新 OAuth2 客户端
     *
     * @param reqVO 更新请求
     */
    void updateClient(OAuth2ClientSaveReqVO reqVO);

    /**
     * 删除 OAuth2 客户端
     *
     * @param id 客户端 ID
     */
    void deleteClient(Long id);

    /**
     * 获得 OAuth2 客户端分页
     *
     * @param pageReqVO 分页查询条件
     * @return 客户端分页
     */
    PageVO<OAuth2ClientRespVO> getClientPage(OAuth2ClientPageReqVO pageReqVO);

    /**
     * 获得 OAuth2 客户端详情
     *
     * @param id 客户端 ID
     * @return 客户端详情
     */
    OAuth2ClientRespVO getClientDetail(Long id);

    /**
     * 根据 clientId 查询客户端
     *
     * @param clientId 客户端 ID
     * @return 客户端
     */
    OAuth2ClientDO getClientByClientId(String clientId);

    /**
     * 根据 ID 查询客户端 DO
     *
     * @param id 主键 ID
     * @return 客户端
     */
    OAuth2ClientDO getClientDOById(Long id);
}
