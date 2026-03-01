#!/bin/bash

# 旅行足迹平台 - 前端启动脚本

echo "========================================"
echo "旅行足迹平台 - 前端服务启动"
echo "========================================"

# 检查Node.js环境
if ! command -v node &> /dev/null; then
    echo "错误: 未找到Node.js环境，请先安装Node.js 16+"
    exit 1
fi

echo "Node.js版本:"
node -version

echo ""
echo "正在启动前端服务..."
echo ""

cd frontend

# 检查node_modules是否存在
if [ ! -d "node_modules" ]; then
    echo "正在安装依赖..."
    npm install
fi

echo "启动开发服务器..."
npm run serve
