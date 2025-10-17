package com.link.JavaStudy.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Link
 * @Description 线程池练习
 * @since 2025/8/15 16:43
 **/
public class ThreadPoolExecutorTest {

    public static void main(String[] args) {
        ThreadPoolExecutor poolExecutor =
                new ThreadPoolExecutor(2, 4, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(3));
        poolExecutor.execute(() -> {
            System.out.println("线程池中的线程数：" + poolExecutor.getPoolSize());
        });
        System.out.println("poolExecutor.getPoolSize() = " + poolExecutor.getPoolSize());
    }

}
