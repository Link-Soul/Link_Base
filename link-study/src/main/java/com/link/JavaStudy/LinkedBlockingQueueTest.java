package com.link.JavaStudy;

import com.alibaba.fastjson2.JSONObject;
import com.link.entity.RequestTask;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 队列的学习
 * @Author Link
 * @Date 2025/4/14 09:59
 */
@RestController
@RequestMapping("/linkedBlockingQueueTest")
public class LinkedBlockingQueueTest {
    private final Integer maxLinkedBlockingQueue = 5;

    // 定义队列，用于存储请求任务
    private final LinkedBlockingQueue<RequestTask> requestQueue = new LinkedBlockingQueue<>(maxLinkedBlockingQueue);
    private final ExecutorService workerThread = Executors.newFixedThreadPool(1);
    private final AtomicBoolean workerStarted = new AtomicBoolean(false);

//    @PostConstruct
    private void init() {
        // 启动消费者线程，后续可以添加
        workerThread.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    RequestTask currentTask = requestQueue.take();
                    Map<String, Object> result = doCheckFaceImage(currentTask.param);
                    currentTask.future.complete(result);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        });
    }


    @PostMapping("/checkFaceImage2")
    public CompletableFuture<Map<String, Object>> checkFaceImage(@RequestBody JSONObject param) {

        System.out.println("进入方法");
        if (workerStarted.compareAndSet(false, true)) {
            init(); // 仅第一次调用时执行
        }
        CompletableFuture<Map<String, Object>> resultFuture = new CompletableFuture<>();
        try {
            // 将请求和Future存入队列（需封装对象）
            RequestTask task = new RequestTask(param, resultFuture);
            requestQueue.put(task);
            return resultFuture;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }


    public Map<String, Object>doCheckFaceImage(JSONObject param){
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("param",param);
        System.out.println("执行方法doCheckFaceImage");
        System.out.println("requestQueue.size() = " + requestQueue.size());
        return objectObjectHashMap;
    }


    public static void main(String[] args) {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(2);

        // 消费线程
        new Thread(() -> {
            try {
                System.out.println("消费线程开始等待...");
                System.out.println("取出: " + queue.take());
                System.out.println("取出: " + queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 生产线程
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                queue.put("A");
                System.out.println("插入: A");

                Thread.sleep(1000);
                queue.put("B");
                System.out.println("插入: B");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


}
