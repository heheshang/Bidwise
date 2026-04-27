#!/bin/bash
# .claude/scripts/oauth2-test-flow.sh
# 测试 OAuth2 四种授权模式

BASE_URL="http://localhost:8080"
CLIENT_ID="test-client"
CLIENT_SECRET="test-secret"

echo "===== OAuth2 流程测试 ====="

# 1. 密码模式
echo -e "\n--- 1. 密码模式 ---"
RESULT=$(curl -s -X POST "$BASE_URL/system/oauth2/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -u "$CLIENT_ID:$CLIENT_SECRET" \
  -d "grant_type=password&username=admin&password=admin123&scope=user.read")
echo "Token Response: $RESULT"
ACCESS_TOKEN=$(echo $RESULT | jq -r '.data.access_token // .access_token')

# 2. 刷新模式
echo -e "\n--- 2. 刷新模式 ---"
REFRESH_TOKEN=$(echo $RESULT | jq -r '.data.refresh_token // .refresh_token')
curl -s -X POST "$BASE_URL/system/oauth2/token" \
  -u "$CLIENT_ID:$CLIENT_SECRET" \
  -d "grant_type=refresh_token&refresh_token=$REFRESH_TOKEN"

# 3. 校验 Token
echo -e "\n--- 3. 校验 Token ---"
curl -s -X POST "$BASE_URL/system/oauth2/check-token" \
  -u "$CLIENT_ID:$CLIENT_SECRET" \
  -d "token=$ACCESS_TOKEN"

# 4. 注销 Token
echo -e "\n--- 4. 注销 Token ---"
curl -s -X DELETE "$BASE_URL/system/oauth2/token" \
  -u "$CLIENT_ID:$CLIENT_SECRET" \
  -d "token=$ACCESS_TOKEN"

echo -e "\n===== 测试完成 ====="
