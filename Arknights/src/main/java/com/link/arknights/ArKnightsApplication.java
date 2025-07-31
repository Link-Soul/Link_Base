package com.link.arknights;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("com.link.arknights.cardpool.mapper")
public class ArKnightsApplication extends SpringBootServletInitializer {
    // 可以用tomcat启动
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ArKnightsApplication.class); // 指向你的启动类
    }

    public static void main(String[] args) {
        SpringApplication.run(ArKnightsApplication.class, args);
    }
}
