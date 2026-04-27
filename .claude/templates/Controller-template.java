// .claude/templates/Controller-template.java
@Tag(name = "管理后台 - OAuth2 ${模块名}")
@RestController
@RequestMapping("/system/oauth2/${路径}")
@Validated
public class OAuth2${模块名}Controller {

    @Resource
    private OAuth2${模块名}Service service;

    @PostMapping("/create")
    @Operation(summary = "创建${模块名}")
    @PreAuthorize("@perm.check('system:oauth2-${路径}:create')")
    public ApiResult<Long> create(@Valid @RequestBody OAuth2${模块名}SaveReqVO reqVO) {
        return success(service.create(reqVO));
    }

    @GetMapping("/page")
    @Operation(summary = "获取${模块名}分页")
    @PreAuthorize("@perm.check('system:oauth2-${路径}:query')")
    public ApiResult<PageData<OAuth2${模块名}RespVO>> page(@Valid OAuth2${模块名}PageReqVO reqVO) {
        PageData<OAuth2${模块名}DO> pageResult = service.getPage(reqVO);
        return success(OAuth2${模块名}Converter.INSTANCE.toPage(pageResult));
    }
}
