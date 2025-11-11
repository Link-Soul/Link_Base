package com.link.arknights;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@MapperScan("com.link.arknights.cardpool.mapper,com.link.core.mapper")
// 扫描core包。core包里引用注解的部分，在执行的这个项目也可以配置对应的配置文件来覆盖
@ComponentScan("com.link")
@Slf4j
public class ArKnightsApplication extends SpringBootServletInitializer {
    // 可以用tomcat启动  继承 SpringBootServletInitializer
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ArKnightsApplication.class); // 指向你的启动类
    }

    public static void main(String[] args) throws Exception {
//        SpringApplication.run(ArKnightsApplication.class, args);
        ConfigurableApplicationContext application = SpringApplication.run(ArKnightsApplication.class, args);

        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "主页: \thttp://{}:{}/index.html\n\t" +
                        "----------------------------------------------------------",
                "明日方舟抽卡信息存储服务",
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }
}
