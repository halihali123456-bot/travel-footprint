# 旅行足迹平台 - 部署指南

## 一、环境准备

### 必需软件
1. **JDK 1.8+** - Java开发环境
2. **Maven 3.6+** - 项目构建工具
3. **MySQL 8.0+** - 数据库
4. **Node.js 16+** - 前端运行环境

### 软件安装参考

#### Ubuntu/Debian
```bash
# JDK
sudo apt update
sudo apt install openjdk-8-jdk

# Maven
sudo apt install maven

# MySQL
sudo apt install mysql-server

# Node.js
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
sudo apt install -y nodejs
```

#### macOS
```bash
# 使用Homebrew安装
brew install openjdk@8
brew install maven
brew install mysql
brew install node@16
```

#### Windows
- JDK: 官网下载安装
- Maven: 官网下载解压并配置环境变量
- MySQL: 下载MySQL Installer安装
- Node.js: 官网下载安装包

## 二、项目部署

### 方式一：自动部署（推荐）

```bash
# 1. 进入项目目录
cd travel-footprint

# 2. 运行初始化脚本
bash setup.sh

# 3. 启动后端服务
bash start-backend.sh

# 4. 新开终端，启动前端服务
bash start-frontend.sh
```

### 方式二：手动部署

#### 1. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 执行SQL脚本
source database/travel_footprint.sql
```

#### 2. 后端部署

```bash
cd backend

# 修改数据库配置
vim src/main/resources/application.yml
# 修改以下配置:
# spring.datasource.url: 数据库连接地址
# spring.datasource.username: 数据库用户名
# spring.datasource.password: 数据库密码

# 编译打包
mvn clean package -DskipTests

# 运行
java -jar target/travel-footprint-1.0.0.jar
```

#### 3. 前端部署

```bash
cd frontend

# 安装依赖
npm install

# 开发模式运行
npm run serve

# 生产打包
npm run build
```

## 三、生产环境部署

### 使用Nginx部署前端

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    location / {
        root /path/to/travel-footprint/frontend/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8080/api;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
    
    location /uploads {
        proxy_pass http://localhost:8080/uploads;
    }
}
```

### 后端服务守护（使用systemd）

创建服务文件 `/etc/systemd/system/travel-footprint.service`:

```ini
[Unit]
Description=Travel Footprint Service
After=syslog.target

[Service]
User=your-user
WorkingDirectory=/path/to/travel-footprint/backend
ExecStart=/usr/bin/java -jar target/travel-footprint-1.0.0.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启动服务:
```bash
sudo systemctl daemon-reload
sudo systemctl enable travel-footprint
sudo systemctl start travel-footprint
```

## 四、常见问题

### 1. 数据库连接失败
- 检查MySQL服务是否运行
- 检查数据库用户名密码是否正确
- 检查数据库是否已创建

### 2. 端口被占用
- 后端默认端口8080，可在application.yml修改
- 前端默认端口3000，可在vue.config.js修改

### 3. 图片上传失败
- 检查uploads目录是否存在且有写入权限
- 检查文件大小是否超过限制

### 4. 跨域问题
- 后端已配置CORS，如仍有问题请检查nginx配置

## 五、系统访问

部署完成后，访问以下地址:

- **前端页面**: http://localhost:3000
- **后端API**: http://localhost:8080/api
- **测试账号**: admin / 123456

## 六、功能模块

| 模块 | 路径 | 说明 |
|------|------|------|
| 首页 | /home | 平台介绍和热门故事 |
| 探索 | /stories | 浏览所有公开故事 |
| 时间线 | /timeline | 个人旅行时间线 |
| 创建故事 | /create | 编写新故事 |
| 个人中心 | /profile | 管理个人信息 |
| 我的故事 | /my-stories | 管理我的故事 |

## 七、联系方式

如有问题，请提交Issue或联系开发者。
