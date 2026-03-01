#!/bin/bash

# 旅行足迹平台 - 后端启动脚本

echo "========================================"
echo "旅行足迹平台 - 后端服务启动"
echo "========================================"

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java环境，请先安装JDK 1.8+"
    exit 1
fi

echo "Java版本:"
java -version

echo ""
echo "正在启动后端服务..."
echo ""

cd backend

# 检查是否有编译好的jar包
if [ -f "target/travel-footprint-1.0.0.jar" ]; then
    echo "使用已编译的jar包启动..."
    java -jar target/travel-footprint-1.0.0.jar
else
    echo "正在编译项目..."
    mvn clean package -DskipTests
    
    if [ $? -eq 0 ]; then
        echo "编译成功，启动服务..."
        java -jar target/travel-footprint-1.0.0.jar
    else
        echo "编译失败，请检查错误信息"
        exit 1
    fi
fi
