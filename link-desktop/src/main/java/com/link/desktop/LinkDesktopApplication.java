package com.link.desktop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.link")
@MapperScan("com.link.desktop.mapper,com.link.core.mapper")
public class LinkDesktopApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkDesktopApplication.class, args);
    }

}