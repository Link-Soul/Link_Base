package com.link.arknights;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.link.arknights.cardpool.mapper")
// 扫描core包。core包里引用注解的部分，在执行的这个项目也可以配置对应的配置文件来覆盖
@ComponentScan("com.link")
public class ArKnightsApplication extends SpringBootServletInitializer {
    // 可以用tomcat启动  继承 SpringBootServletInitializer
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ArKnightsApplication.class); // 指向你的启动类
    }

    public static void main(String[] args) {
        SpringApplication.run(ArKnightsApplication.class, args);
    }
}
