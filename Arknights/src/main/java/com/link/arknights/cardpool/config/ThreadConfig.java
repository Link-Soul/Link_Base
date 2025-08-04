package com.link.arknights.cardpool.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 开启异步任务
 */
@Configuration
@EnableAsync //开启异步任务
public class ThreadConfig {
    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(4);
        //配置最大线程数
        executor.setMaxPoolSize(8);
        //配置队列大小
        executor.setQueueCapacity(1000);
        //线程名称前缀
        executor.setThreadNamePrefix("LinkExecutor-");
        //线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        //等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //设置拒绝策略
        //executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
