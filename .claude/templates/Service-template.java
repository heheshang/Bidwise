// .claude/templates/Service-template.java
public interface OAuth2${模块名}Service {

    /**
     * 创建 OAuth2 ${模块名}
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long create(@Valid OAuth2${模块名}SaveReqVO createReqVO);

    /**
     * 更新 OAuth2 ${模块名}
     *
     * @param updateReqVO 更新信息
     */
    void update(@Valid OAuth2${模块名}SaveReqVO updateReqVO);

    /**
     * 删除 OAuth2 ${模块名}
     *
     * @param id 编号
     */
    void delete(Long id);

    /**
     * 获得 OAuth2 ${模块名}分页
     *
     * @param pageReqVO 分页查询
     * @return 分页结果
     */
    PageData<OAuth2${模块名}DO> getPage(OAuth2${模块名}PageReqVO pageReqVO);
}
