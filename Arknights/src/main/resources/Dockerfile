# 使用官方的 Java 8 镜像作为基础镜像
FROM tomcat:9.0-jdk8-openjdk

# 设置工作目录
WORKDIR /app

# 将打包好的 JAR 文件复制到容器中
COPY target/myapp-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]