# Link_Base 项目说明

## 项目概述

Link_Base 是一个基于 Spring Boot 2.7.18 的多模块 Java 项目，使用 Java 8 开发。该项目采用 Maven 多模块架构，包含四个主要子模块：

- **Arknights**: 明日方舟抽卡信息存储服务，提供 Web 界面和 API
- **link-core**: 核心工具库和通用功能模块
- **link-study**: 学习和实验模块，包含 AI、图像处理等功能
- **link-vue**: Vue.js 前端桌面应用，模拟操作系统界面

## 技术栈

### 后端技术
- **框架**: Spring Boot 2.7.18
- **数据库**: MySQL 8.0.33
- **ORM**: MyBatis Plus 3.5.4.1
- **连接池**: Druid 1.2.18
- **工具库**: Lombok 1.18.24, Hutool 5.8.15, FastJSON2 2.0.57
- **HTTP客户端**: Apache HttpClient5 5.4.4
- **WebSocket**: Java-WebSocket 1.5.1
- **其他**: OpenCV 4.7.0-1.5.9 (图像处理), JNativeHook 2.2.2 (键盘鼠标监听)

### 前端技术 (link-vue)
- **框架**: Vue 3.2.13
- **路由**: Vue Router 4.0.3
- **状态管理**: Pinia 2.1.7
- **构建工具**: Vue CLI 5.0.0
- **样式**: Sass/SCSS
- **PWA**: 支持离线使用

## 项目结构

```
Link_Base/
├── Arknights/          # 明日方舟抽卡系统
│   ├── src/main/java/  # Java 源码
│   ├── src/main/resources/  # 配置文件
│   └── src/main/webapp/  # Web 资源 (JSP, CSS, JS)
├── link-core/          # 核心工具库
│   └── src/main/java/  # 通用工具类和服务
├── link-study/         # 学习实验模块
│   └── src/main/java/  # AI、图像处理等实验代码
├── link-vue/           # Vue.js 前端桌面应用
│   ├── src/components/ # Vue 组件
│   ├── src/views/      # 页面视图
│   ├── src/stores/     # Pinia 状态管理
│   └── src/styles/     # 全局样式
├── sql/                # 数据库脚本
└── log/                # 日志文件
```

## 构建和运行

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Node.js 14+ (用于 link-vue 模块)

### 构建命令

#### Java 后端模块
```bash
# 在项目根目录执行
mvn clean install

# 或构建特定模块
mvn clean install -pl Arknights
mvn clean install -pl link-core
mvn clean install -pl link-study
```

#### Vue.js 前端模块
```bash
cd link-vue
npm install
npm run serve  # 开发模式
npm run build  # 生产构建
```

### 运行命令

#### Arknights 模块 (端口: 8096)
```bash
cd Arknights
mvn spring-boot:run
```
访问地址: http://localhost:8096/index.html

#### link-core 模块
```bash
cd link-core
mvn spring-boot:run
```

#### link-study 模块 (端口: 8080)
```bash
cd link-study
mvn spring-boot:run
```

#### link-vue 模块 (端口: 8081)
```bash
cd link-vue
npm run serve
```
访问地址: http://localhost:8081

## 开发规范

### 代码结构
- 遵循标准的 Maven 项目结构
- 使用 Spring Boot 约定优于配置的原则
- 实体类放在 `entity` 包下
- 数据访问层使用 `mapper` 包 (MyBatis)
- 业务逻辑层使用 `service` 包
- 控制器使用 `controller` 包

### 配置管理
- 主配置文件: `application.yaml`
- 数据库配置在每个模块的配置文件中独立管理
- 使用 Spring Boot 的配置属性绑定机制

### 数据库
- 使用 MyBatis Plus 作为 ORM 框架
- 支持自动驼峰命名转换
- 配置了 SQL 日志输出 (生产环境建议关闭)

### 依赖管理
- 父 POM 统一管理版本号
- 子模块按需引入依赖
- 使用 `dependencyManagement` 进行版本控制

### 前端开发规范
- 使用 Vue 3 Composition API
- 组件化开发，组件放在 `components` 目录
- 页面放在 `views` 目录
- 使用 Pinia 进行状态管理
- 遵循 ESLint 代码规范

## 主要功能模块

### Arknights 模块
- 抽卡信息存储和查询
- Web 界面展示 (JSP)
- RESTful API 接口
- 邮件通知功能 (支持 QQ 和 163 邮箱)
- 文件上传功能

### link-core 模块
- 通用工具类 (日期、文件、HTTP 等)
- Redis 支持
- 邮件服务
- WebSocket 支持
- 拼音转换功能

### link-study 模块
- AI 相关功能 (语音识别、自然语言处理)
- 图像处理 (OpenCV)
- 屏幕颜色检测
- 键盘鼠标事件监听
- WebSocket 通信实验

### link-vue 模块
- 桌面环境模拟
- 窗口管理系统
- 应用程序框架 (计算器、笔记、设置等)
- 壁纸管理
- PWA 支持

## 部署说明

### 传统部署
1. 执行 `mvn clean package` 打包
2. 将 WAR 文件部署到 Tomcat 服务器
3. 配置数据库连接参数

### 前端部署
```bash
cd link-vue
npm run build
# 将 dist 目录部署到 Web 服务器
```

### Docker 部署
项目包含 Dockerfile 配置，支持容器化部署

## 端口配置

| 模块 | 端口 | 说明 |
|------|------|------|
| Arknights | 8096 | 明日方舟抽卡系统 |
| link-study | 8080 | 学习实验模块 |
| link-vue | 8081 | Vue.js 前端应用 |

## 注意事项

- 生产环境部署时请修改数据库密码等敏感配置
- 建议使用配置中心管理不同环境的配置
- 日志级别可根据需要进行调整
- 文件上传路径需要根据实际环境配置
- link-vue 模块需要独立的 Node.js 环境
- 各模块的数据库连接配置独立管理，注意端口差异