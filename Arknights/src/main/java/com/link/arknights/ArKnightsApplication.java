package com.link.arknights;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.link.arknights.cardpool.mapper")
public class ArKnightsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArKnightsApplication.class, args);
    }
}
