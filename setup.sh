#!/bin/bash

# 旅行足迹平台 - 项目初始化脚本

echo "========================================"
echo "旅行足迹平台 - 项目初始化"
echo "========================================"
echo ""

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 检查Java环境
echo "[1/5] 检查Java环境..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo -e "${GREEN}✓ Java已安装: $JAVA_VERSION${NC}"
else
    echo -e "${RED}✗ 未找到Java环境，请先安装JDK 1.8+${NC}"
    exit 1
fi

# 检查Maven环境
echo ""
echo "[2/5] 检查Maven环境..."
if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn -version 2>&1 | head -n 1)
    echo -e "${GREEN}✓ Maven已安装${NC}"
else
    echo -e "${RED}✗ 未找到Maven环境，请先安装Maven 3.6+${NC}"
    exit 1
fi

# 检查Node.js环境
echo ""
echo "[3/5] 检查Node.js环境..."
if command -v node &> /dev/null; then
    NODE_VERSION=$(node --version)
    echo -e "${GREEN}✓ Node.js已安装: $NODE_VERSION${NC}"
else
    echo -e "${RED}✗ 未找到Node.js环境，请先安装Node.js 16+${NC}"
    exit 1
fi

# 检查MySQL环境
echo ""
echo "[4/5] 检查MySQL环境..."
if command -v mysql &> /dev/null; then
    echo -e "${GREEN}✓ MySQL客户端已安装${NC}"
else
    echo -e "${YELLOW}! 未找到MySQL客户端，请确保MySQL服务已安装并运行${NC}"
fi

# 初始化数据库
echo ""
echo "[5/5] 初始化数据库..."
echo "请输入MySQL root用户密码:"
read -s MYSQL_PASSWORD

echo "正在创建数据库..."
mysql -u root -p$MYSQL_PASSWORD < database/travel_footprint.sql

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ 数据库初始化成功${NC}"
else
    echo -e "${RED}✗ 数据库初始化失败，请检查密码是否正确${NC}"
    exit 1
fi

# 编译后端
echo ""
echo "正在编译后端项目..."
cd backend
mvn clean compile -DskipTests

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ 后端编译成功${NC}"
else
    echo -e "${RED}✗ 后端编译失败${NC}"
    exit 1
fi

cd ..

# 安装前端依赖
echo ""
echo "正在安装前端依赖..."
cd frontend
npm install

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ 前端依赖安装成功${NC}"
else
    echo -e "${RED}✗ 前端依赖安装失败${NC}"
    exit 1
fi

cd ..

echo ""
echo "========================================"
echo -e "${GREEN}项目初始化完成！${NC}"
echo "========================================"
echo ""
echo "启动项目:"
echo "  1. 启动后端: bash start-backend.sh"
echo "  2. 启动前端: bash start-frontend.sh"
echo ""
echo "默认访问地址:"
echo "  前端: http://localhost:3000"
echo "  后端: http://localhost:8080/api"
echo ""
echo "测试账号:"
echo "  用户名: admin"
echo "  密码: 123456"
echo ""
