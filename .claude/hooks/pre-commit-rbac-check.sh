#!/bin/bash
# .claude/hooks/pre-commit-rbac-check.sh
# 提交前检查 RBAC 安全红线

echo "🔒 RBAC 安全检查开始..."

ERRORS=0

# 检查1：是否硬编码角色判断
if grep -rn 'role.*==.*"admin"\|role.*equals.*"admin"\|getRole.*==\|\.getCode.*==' --include="*.java" src/ | grep -v "test" | grep -v "Test" | grep -v "RoleCodeEnum" | grep -v "Enum"; then
    echo "❌ 发现硬编码角色判断！请使用权限标识 @perm.check()"
    ERRORS=$((ERRORS + 1))
fi

# 检查2：是否有未加权限注解的 Controller 方法
if grep -rn '@PostMapping\|@PutMapping\|@DeleteMapping' --include="*.java" src/main/java/ | grep -v "test" | grep -v "Test" | while read line; do
    FILE=$(echo "$line" | cut -d: -f1)
    LINE_NUM=$(echo "$line" | cut -d: -f2)
    PREV_LINES=$(sed -n "$((LINE_NUM-3)),$((LINE_NUM))p" "$FILE")
    if ! echo "$PREV_LINES" | grep -q "@PreAuthorize\|@PermitAll"; then
        echo "⚠️ $FILE:$LINE_NUM 缺少 @PreAuthorize 或 @PermitAll"
        exit 1
    fi
done; then
    ERRORS=$((ERRORS + 1))
fi

# 检查3：菜单权限标识是否有重复
DUPLICATES=$(grep -rn 'permission.*=.*"' --include="*.java" src/ | grep -v "test" | grep -oP 'permission\s*=\s*"\K[^"]+' | sort | uniq -d)
if [ -n "$DUPLICATES" ]; then
    echo "❌ 发现重复的权限标识：$DUPLICATES"
    ERRORS=$((ERRORS + 1))
fi

# 检查4：是否直接删除内置角色
if grep -rn 'deleteById.*\|removeById.*' --include="*.java" src/ | grep -i "role" | grep -v "test" | grep -v "Test" | grep -v "validateRoleCanDelete"; then
    echo "⚠️ 警告：删除角色时请确保校验了角色类型（内置角色不可删除）"
fi

# 检查5：权限变更是否清除缓存
if grep -rn 'assignRoleMenu\|assignUserRole\|deleteRole\|deleteMenu' --include="*.java" src/ | grep -v "test" | grep -v "Test" | grep -v "Cache\|cache\|evict\|delete.*Redis\|remove.*Cache"; then
    echo "⚠️ 警告：权限变更操作请确保清除了 Redis 缓存"
fi

if [ $ERRORS -gt 0 ]; then
    echo "🚫 安全检查未通过，共 $ERRORS 个问题需要修复"
    exit 1
fi

echo "✅ RBAC 安全检查通过"
