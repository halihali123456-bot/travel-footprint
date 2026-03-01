# 旅行足迹记录与分享平台

## 项目简介

这是一个基于 Spring Boot + Vue.js 的现代化旅行足迹记录与分享平台，旨在帮助用户记录旅途中的美好瞬间，通过文字和图片保存珍贵回忆，并与志同道合的旅行者分享交流。

## 核心功能

- **旅行故事创建**: 支持富文本编辑、图片上传、地点标记
- **时间线视图**: 按时间顺序回顾旅行历程
- **故事分享**: 支持公开/私密设置，点赞互动
- **图片上传**: 支持多图上传，图片预览
- **搜索发现**: 按关键词搜索目的地和故事

## 技术架构

### 后端技术栈
- **Spring Boot 2.7.14**: 核心框架
- **MyBatis Plus**: ORM框架
- **MySQL 8.0**: 数据库
- **JWT**: 用户认证
- **Spring Security Crypto**: 密码加密
- **Maven**: 项目构建

### 前端技术栈
- **Vue.js 3**: 前端框架
- **Vue Router 4**: 路由管理
- **Vuex 4**: 状态管理
- **Element Plus**: UI组件库
- **Axios**: HTTP请求

## 项目结构

```
travel-footprint/
├── backend/                    # Spring Boot后端
│   ├── src/main/java/com/travel/footprint/
│   │   ├── config/            # 配置类
│   │   ├── controller/        # 控制器
│   │   ├── entity/            # 实体类
│   │   ├── mapper/            # MyBatis映射器
│   │   ├── service/           # 服务层
│   │   ├── utils/             # 工具类
│   │   ├── dto/               # 数据传输对象
│   │   └── vo/                # 视图对象
│   ├── src/main/resources/
│   │   └── application.yml    # 配置文件
│   └── pom.xml                # Maven配置
├── frontend/                   # Vue前端
│   ├── src/
│   │   ├── api/               # API接口
│   │   ├── assets/            # 静态资源
│   │   ├── components/        # 组件
│   │   ├── router/            # 路由配置
│   │   ├── store/             # Vuex状态
│   │   ├── utils/             # 工具函数
│   │   └── views/             # 页面视图
│   ├── package.json
│   └── vue.config.js
├── database/
│   └── travel_footprint.sql   # 数据库脚本
└── README.md
```

## 数据库设计

### 用户表 (user)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名 |
| password | VARCHAR(100) | 密码(加密) |
| nickname | VARCHAR(50) | 昵称 |
| avatar | VARCHAR(255) | 头像URL |
| email | VARCHAR(100) | 邮箱 |
| create_time | DATETIME | 创建时间 |

### 旅行故事表 (travel_story)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| title | VARCHAR(200) | 标题 |
| content | TEXT | 内容 |
| location | VARCHAR(200) | 地点 |
| latitude | DECIMAL | 纬度 |
| longitude | DECIMAL | 经度 |
| travel_date | DATE | 旅行日期 |
| cover_image | VARCHAR(255) | 封面图片 |
| is_public | TINYINT | 是否公开 |
| view_count | INT | 浏览次数 |
| like_count | INT | 点赞次数 |

### 故事图片表 (story_image)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| story_id | BIGINT | 故事ID |
| image_url | VARCHAR(255) | 图片URL |
| description | VARCHAR(500) | 图片描述 |
| sort_order | INT | 排序 |

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Node.js 16+

### 1. 数据库初始化

```bash
# 登录MySQL
mysql -u root -p

# 执行数据库脚本
source database/travel_footprint.sql
```

### 2. 后端启动

```bash
# 进入后端目录
cd backend

# 修改数据库配置 (src/main/resources/application.yml)
# 修改数据库用户名和密码

# 编译运行
mvn clean install
mvn spring-boot:run
```

后端服务默认运行在 http://localhost:8080/api

### 3. 前端启动

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 开发模式运行
npm run serve
```

前端服务默认运行在 http://localhost:3000

### 4. 生产部署

#### 后端打包
```bash
cd backend
mvn clean package
# 生成的jar包在 target/travel-footprint-1.0.0.jar
```

#### 前端打包
```bash
cd frontend
npm run build
# 生成的静态文件在 dist/ 目录
```

## API接口文档

### 用户接口
| 接口 | 方法 | 说明 |
|------|------|------|
| /user/login | POST | 用户登录 |
| /user/register | POST | 用户注册 |
| /user/info | GET | 获取用户信息 |
| /user/info | PUT | 更新用户信息 |

### 故事接口
| 接口 | 方法 | 说明 |
|------|------|------|
| /story | POST | 创建故事 |
| /story/{id} | PUT | 更新故事 |
| /story/{id} | DELETE | 删除故事 |
| /story/{id} | GET | 获取故事详情 |
| /story/public | GET | 获取公开故事列表 |
| /story/my | GET | 获取我的故事 |
| /story/search | GET | 搜索故事 |
| /story/hot | GET | 获取热门故事 |
| /story/timeline | GET | 获取时间线 |
| /story/{id}/like | POST | 点赞 |
| /story/{id}/like | DELETE | 取消点赞 |

### 上传接口
| 接口 | 方法 | 说明 |
|------|------|------|
| /upload/image | POST | 上传图片 |

## 系统截图

- 首页: 展示平台介绍和热门故事
- 探索页: 浏览和搜索所有公开故事
- 故事详情: 查看故事内容和图片
- 时间线: 按时间回顾个人旅行历程
- 创建故事: 编写故事和上传图片
- 个人中心: 管理个人信息和故事

## 开发团队

本项目为本科毕业设计作品。

## 许可证

MIT License
