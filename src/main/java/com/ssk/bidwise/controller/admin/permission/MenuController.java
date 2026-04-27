package com.ssk.bidwise.controller.admin.permission;

import com.ssk.bidwise.common.result.Result;
import com.ssk.bidwise.controller.admin.permission.vo.menu.MenuRespVO;
import com.ssk.bidwise.controller.admin.permission.vo.menu.MenuSaveReqVO;
import com.ssk.bidwise.service.permission.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssk.bidwise.common.result.Result.success;

/**
 * 菜单管理 Controller
 *
 * @author Bidwise
 */
@Tag(name = "管理后台 - 菜单管理")
@RestController
@RequestMapping("/api/v1/permission/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @PostMapping("/create")
    @Operation(summary = "创建菜单")
    @PreAuthorize("@perm.check('system:menu:create')")
    public Result<Long> create(@Valid @RequestBody MenuSaveReqVO reqVO) {
        return success(menuService.createMenu(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新菜单")
    @PreAuthorize("@perm.check('system:menu:update')")
    public Result<Void> update(@Valid @RequestBody MenuSaveReqVO reqVO) {
        menuService.updateMenu(reqVO);
        return success();
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除菜单")
    @PreAuthorize("@perm.check('system:menu:delete')")
    public Result<Void> delete(@RequestParam("id") Long id) {
        menuService.deleteMenu(id);
        return success();
    }

    @GetMapping("/get")
    @Operation(summary = "获取菜单详情")
    @PreAuthorize("@perm.check('system:menu:query')")
    public Result<MenuRespVO> get(@RequestParam("id") Long id) {
        return success(menuService.getMenu(id));
    }

    @GetMapping("/list")
    @Operation(summary = "获取菜单树形列表")
    @PreAuthorize("@perm.check('system:menu:query')")
    public Result<List<MenuRespVO>> list() {
        return success(menuService.getMenuList());
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获取菜单精简树形列表，用于下拉框")
    public Result<List<MenuRespVO>> simpleList() {
        return success(menuService.getMenuSimpleList());
    }

}
