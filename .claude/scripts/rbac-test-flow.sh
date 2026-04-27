#!/bin/bash
# .claude/scripts/rbac-test-flow.sh
# 测试 RBAC 权限管理完整流程

BASE_URL="http://localhost:8080"
TOKEN="your-admin-token-here"
AUTH="Authorization: Bearer $TOKEN"

echo "===== RBAC 权限管理流程测试 ====="

# 1. 创建菜单（三级结构）
echo -e "\n--- 1. 创建菜单 ---"
# 一级目录
DIR_RESULT=$(curl -s -X POST "$BASE_URL/permission/menu/create" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d '{"name":"系统管理","type":1,"parentId":0,"sort":1,"path":"system","icon":"setting"}')
echo "目录创建: $DIR_RESULT"
DIR_ID=$(echo $DIR_RESULT | jq -r '.data')

# 二级菜单
MENU_RESULT=$(curl -s -X POST "$BASE_URL/permission/menu/create" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d "{\"name\":\"用户管理\",\"type\":2,\"parentId\":$DIR_ID,\"sort\":1,\"path\":\"user\",\"component\":\"system/user/index\"}")
echo "菜单创建: $MENU_RESULT"
MENU_ID=$(echo $MENU_RESULT | jq -r '.data')

# 三级按钮
BTN_RESULT=$(curl -s -X POST "$BASE_URL/permission/menu/create" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d "{\"name\":\"用户新增\",\"type\":3,\"parentId\":$MENU_ID,\"sort\":1,\"permission\":\"system:user:create\"}")
echo "按钮创建: $BTN_RESULT"
BTN_ID=$(echo $BTN_RESULT | jq -r '.data')

# 2. 创建角色
echo -e "\n--- 2. 创建角色 ---"
ROLE_RESULT=$(curl -s -X POST "$BASE_URL/permission/role/create" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d '{"name":"测试角色","code":"test_role","sort":99,"status":0,"remark":"RBAC流程测试"}')
echo "角色创建: $ROLE_RESULT"
ROLE_ID=$(echo $ROLE_RESULT | jq -r '.data')

# 3. 分配角色菜单
echo -e "\n--- 3. 分配角色菜单 ---"
curl -s -X POST "$BASE_URL/permission/assign-role-menu" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d "{\"roleId\":$ROLE_ID,\"menuIds\":[$DIR_ID,$MENU_ID,$BTN_ID]}"

# 4. 验证角色菜单
echo -e "\n--- 4. 验证角色菜单 ---"
curl -s "$BASE_URL/permission/role-menu-list?roleId=$ROLE_ID" -H "$AUTH"

# 5. 分配用户角色
echo -e "\n--- 5. 分配用户角色 ---"
curl -s -X POST "$BASE_URL/permission/assign-user-role" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d "{\"userId\":1,\"roleIds\":[$ROLE_ID]}"

# 6. 清理测试数据
echo -e "\n--- 6. 清理测试数据 ---"
curl -s -X DELETE "$BASE_URL/permission/role/delete?id=$ROLE_ID" -H "$AUTH"
curl -s -X DELETE "$BASE_URL/permission/menu/delete?id=$BTN_ID" -H "$AUTH"
curl -s -X DELETE "$BASE_URL/permission/menu/delete?id=$MENU_ID" -H "$AUTH"
curl -s -X DELETE "$BASE_URL/permission/menu/delete?id=$DIR_ID" -H "$AUTH"

echo -e "\n===== 测试完成 ====="
