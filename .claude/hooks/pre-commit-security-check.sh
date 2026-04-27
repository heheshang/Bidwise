#!/bin/bash
# .claude/hooks/pre-commit-security-check.sh
# 提交前检查 OAuth2 安全红线

echo "🔒 OAuth2 安全检查开始..."

ERRORS=0

# 检查1：密钥是否硬编码
if grep -rn "client_secret.*=.*\"" --include="*.java" src/ | grep -v "test" | grep -v "Test"; then
    echo "❌ 发现硬编码的 client_secret！"
    ERRORS=$((ERRORS + 1))
fi

# 检查2：Token 是否放在 URL 参数
if grep -rn "access_token=" --include="*.java" src/ | grep -v "test" | grep -v "Test" | grep -v "removeToken"; then
    echo "❌ 发现 Token 放在 URL 参数中！"
    ERRORS=$((ERRORS + 1))
fi

# 检查3：是否有 token 永不过期的配置
if grep -rn "setExpiresTime.*null\|validitySeconds.*-1\|validitySeconds.*0" --include="*.java" src/; then
    echo "❌ 发现 Token 永不过期的配置！"
    ERRORS=$((ERRORS + 1))
fi

# 检查4：日志中是否打印了完整 token
if grep -rn "log\.\(info\|debug\|warn\|error\).*accessToken\|log\.\(info\|debug\|warn\|error\).*refreshToken\|log\.\(info\|debug\|warn\|error\).*secret" --include="*.java" src/ | grep -v "test" | grep -v "Test"; then
    echo "⚠️ 警告：日志中可能包含敏感 Token 信息"
    ERRORS=$((ERRORS + 1))
fi

# 检查5：CORS 是否配置了 *
if grep -rn "allowedOrigins.*\*\|allow-origin.*\*" --include="*.java" --include="*.yml" --include="*.yaml" src/ resources/; then
    echo "❌ 发现 CORS allow-origin: * 配置！"
    ERRORS=$((ERRORS + 1))
fi

if [ $ERRORS -gt 0 ]; then
    echo "🚫 安全检查未通过，共 $ERRORS 个问题需要修复"
    exit 1
fi

echo "✅ OAuth2 安全检查通过"
