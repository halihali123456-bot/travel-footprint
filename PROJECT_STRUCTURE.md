# 项目结构说明

## 目录结构

```
travel-footprint/                          # 项目根目录
├── README.md                              # 项目说明文档
├── DEPLOY.md                              # 部署指南
├── PROJECT_STRUCTURE.md                   # 项目结构说明
├── setup.sh                               # 项目初始化脚本
├── start-backend.sh                       # 后端启动脚本
├── start-frontend.sh                      # 前端启动脚本
│
├── database/                              # 数据库
│   └── travel_footprint.sql               # 数据库初始化脚本
│
├── backend/                               # Spring Boot后端
│   ├── pom.xml                            # Maven配置文件
│   └── src/
│       ├── main/
│       │   ├── java/com/travel/footprint/
│       │   │   ├── TravelFootprintApplication.java    # 启动类
│       │   │   │
│       │   │   ├── config/                # 配置类
│       │   │   │   ├── GlobalExceptionHandler.java    # 全局异常处理
│       │   │   │   ├── JwtInterceptor.java            # JWT拦截器
│       │   │   │   ├── MyBatisPlusConfig.java         # MyBatisPlus配置
│       │   │   │   ├── MyMetaObjectHandler.java       # 自动填充配置
│       │   │   │   └── WebMvcConfig.java              # WebMVC配置
│       │   │   │
│       │   │   ├── controller/            # 控制器层
│       │   │   │   ├── TravelStoryController.java     # 故事接口
│       │   │   │   ├── UploadController.java          # 上传接口
│       │   │   │   └── UserController.java            # 用户接口
│       │   │   │
│       │   │   ├── dto/                   # 数据传输对象
│       │   │   │   ├── ImageDTO.java
│       │   │   │   ├── LoginDTO.java
│       │   │   │   ├── RegisterDTO.java
│       │   │   │   └── StoryDTO.java
│       │   │   │
│       │   │   ├── entity/                # 实体类
│       │   │   │   ├── StoryImage.java
│       │   │   │   ├── StoryLike.java
│       │   │   │   ├── TravelStory.java
│       │   │   │   └── User.java
│       │   │   │
│       │   │   ├── mapper/                # MyBatis映射器
│       │   │   │   ├── StoryImageMapper.java
│       │   │   │   ├── StoryLikeMapper.java
│       │   │   │   ├── TravelStoryMapper.java
│       │   │   │   └── UserMapper.java
│       │   │   │
│       │   │   ├── service/               # 服务层
│       │   │   │   ├── impl/              # 服务实现
│       │   │   │   │   ├── StoryImageServiceImpl.java
│       │   │   │   │   ├── TravelStoryServiceImpl.java
│       │   │   │   │   └── UserServiceImpl.java
│       │   │   │   ├── StoryImageService.java
│       │   │   │   ├── TravelStoryService.java
│       │   │   │   └── UserService.java
│       │   │   │
│       │   │   ├── utils/                 # 工具类
│       │   │   │   ├── FileUploadUtils.java
│       │   │   │   ├── JwtUtils.java
│       │   │   │   └── PasswordUtils.java
│       │   │   │
│       │   │   └── vo/                    # 视图对象
│       │   │       ├── PageVO.java
│       │   │       ├── ResultVO.java
│       │   │       └── UserVO.java
│       │   │
│       │   └── resources/
│       │       └── application.yml        # 应用配置文件
│       │
│       └── test/                          # 测试代码
│
└── frontend/                              # Vue前端
    ├── package.json                       # NPM配置
    ├── vue.config.js                      # Vue配置
    ├── public/
    │   └── index.html                     # HTML模板
    │
    └── src/
        ├── main.js                        # 入口文件
        ├── App.vue                        # 根组件
        │
        ├── api/                           # API接口
        │   ├── story.js                   # 故事相关接口
        │   ├── upload.js                  # 上传接口
        │   └── user.js                    # 用户相关接口
        │
        ├── assets/                        # 静态资源
        │
        ├── components/                    # 公共组件
        │
        ├── router/                        # 路由配置
        │   └── index.js
        │
        ├── store/                         # Vuex状态管理
        │   └── index.js
        │
        ├── utils/                         # 工具函数
        │   └── request.js                 # Axios请求封装
        │
        └── views/                         # 页面视图
            ├── CreateStory.vue            # 创建/编辑故事
            ├── Home.vue                   # 首页
            ├── Layout.vue                 # 布局组件
            ├── Login.vue                  # 登录页
            ├── MyStories.vue              # 我的故事
            ├── Profile.vue                # 个人中心
            ├── Register.vue               # 注册页
            ├── Stories.vue                # 故事列表
            ├── StoryDetail.vue            # 故事详情
            └── Timeline.vue               # 时间线
```

## 核心功能模块

### 1. 用户模块 (User)
- 用户注册/登录
- JWT Token认证
- 用户信息管理
- 密码加密存储

### 2. 故事模块 (TravelStory)
- 故事的增删改查
- 故事列表分页
- 故事搜索
- 热门故事推荐
- 浏览量/点赞数统计

### 3. 图片模块 (StoryImage)
- 多图上传
- 图片预览
- 图片轮播

### 4. 点赞模块 (StoryLike)
- 点赞/取消点赞
- 点赞数统计

### 5. 时间线模块 (Timeline)
- 按时间排序展示
- 可视化时间轴

## 技术栈说明

### 后端
| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.7.14 | 核心框架 |
| MyBatis Plus | 3.5.3.1 | ORM框架 |
| MySQL | 8.0+ | 数据库 |
| JWT | 4.4.0 | 身份认证 |
| Maven | 3.6+ | 构建工具 |

### 前端
| 技术 | 版本 | 用途 |
|------|------|------|
| Vue.js | 3.3.0 | 前端框架 |
| Vue Router | 4.2.0 | 路由管理 |
| Vuex | 4.1.0 | 状态管理 |
| Element Plus | 2.4.0 | UI组件库 |
| Axios | 1.6.0 | HTTP请求 |

## 数据库表结构

### 用户表 (user)
存储用户基本信息，包括用户名、密码、昵称、头像等。

### 旅行故事表 (travel_story)
存储故事内容，包括标题、内容、地点、旅行日期、封面图等。

### 故事图片表 (story_image)
存储故事关联的图片信息。

### 点赞记录表 (story_like)
存储用户点赞记录。

## 接口设计

所有接口返回统一格式:
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 状态码
| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 401 | 未授权 |
| 500 | 服务器错误 |

## 安全设计

1. **密码加密**: 使用BCrypt算法加密存储
2. **JWT认证**: 使用JWT Token进行身份验证
3. **接口鉴权**: 拦截器验证Token有效性
4. **SQL注入防护**: 使用MyBatis参数化查询

## 扩展建议

1. 添加评论功能
2. 集成地图组件显示位置
3. 添加标签分类
4. 实现消息通知
5. 添加数据统计图表
