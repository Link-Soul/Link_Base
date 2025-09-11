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

    @Value("${spring.web.loginurl}")
    private String loginUrl;

    @Value("${spring.auto.openurl}")
    private boolean isOpen;

    @Override
    public void run(String... args) throws Exception {
        if (isOpen) {

            // 只在Windows系统下执行自动打开浏览器的操作
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                // Windows系统下使用rundll32
                Runtime run = Runtime.getRuntime();
                try {
                    // 获取默认浏览器，有点吊
                    run.exec("rundll32 url.dll,FileProtocolHandler " + loginUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // 可选：在Linux系统下可以使用xdg-open（如果容器中有图形环境）
                // 或者直接注释掉，不在Linux环境下自动打开浏览器
                System.out.println("Linux环境下不自动打开浏览器");
            }
        }
    }
}