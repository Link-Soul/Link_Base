package com.link.arknights.cardpool.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author ：
 * @date ：Created in 2022/11/13 23:41
 * @description：配置自动打开浏览器
 * @modified By：
 * @version:1.0
 */
@Component    //该注解把类实例化到spring容器中相当于配置文件
public class MyBrowserRunner implements CommandLineRunner {

    //框架自带的日志 打印信息到控制台
//    private static Logger logger = LoggerFactory.getLogger(MyBrowserRunner.class);

    //通过该注解@Value 利用spel表达式(${spring.web.loginurl})获取配置文件的值
    @Value("${spring.web.loginurl}")
    private String loginUrl;

    @Value("${spring.auto.openurl}")
    private boolean isOpen;

    @Override
    public void run(String... args) throws Exception {
        if (isOpen) {
            Runtime run = Runtime.getRuntime();
            try {
                // 获取默认浏览器，有点吊
                run.exec("rundll32 url.dll,FileProtocolHandler " + loginUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}