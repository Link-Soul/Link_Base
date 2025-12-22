# Link_Base 项目说明

## 项目概述

Link_Base 是一个基于 Spring Boot 2.7.18 的多模块 Java 项目，使用 Java 8 开发。该项目采用 Maven 多模块架构，包含三个主要子模块：

- **Arknights**: 明日方舟抽卡信息存储服务，提供 Web 界面和 API
- **link-core**: 核心工具库和通用功能模块
- **link-study**: 学习和实验模块，包含 AI、图像处理等功能

## 技术栈

- **框架**: Spring Boot 2.7.18
- **数据库**: MySQL 8.0.33
- **ORM**: MyBatis Plus 3.5.4.1
- **连接池**: Druid 1.2.18
- **工具库**: Lombok, Hutool, FastJSON2
- **HTTP客户端**: Apache HttpClient5
- **WebSocket**: Java-WebSocket
- **其他**: OpenCV (图像处理), JNativeHook (键盘鼠标监听)

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
├── sql/                # 数据库脚本
└── log/                # 日志文件
```

## 构建和运行

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+

### 构建命令
```bash
# 在项目根目录执行
mvn clean install

# 或构建特定模块
mvn clean install -pl Arknights
mvn clean install -pl link-core
mvn clean install -pl link-study
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

## 主要功能模块

### Arknights 模块
- 抽卡信息存储和查询
- Web 界面展示 (JSP)
- RESTful API 接口
- 邮件通知功能

### link-core 模块
- 通用工具类 (日期、文件、HTTP 等)
- Redis 支持
- 邮件服务
- WebSocket 支持

### link-study 模块
- AI 相关功能 (语音识别、自然语言处理)
- 图像处理 (OpenCV)
- 屏幕颜色检测
- 键盘鼠标事件监听

## 部署说明

### 传统部署
1. 执行 `mvn clean package` 打包
2. 将 WAR 文件部署到 Tomcat 服务器
3. 配置数据库连接参数

### Docker 部署
项目包含 Dockerfile 配置，支持容器化部署

## 注意事项

- 生产环境部署时请修改数据库密码等敏感配置
- 建议使用配置中心管理不同环境的配置
- 日志级别可根据需要进行调整
- 文件上传路径需要根据实际环境配置