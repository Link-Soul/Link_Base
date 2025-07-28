package com.link.v2.thread.java;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AIChatAgentTest {

//    private static final String API_URL = "http://v2.alpha.kaerplatform.com/ai/helper-student/ai-chat-stream"; // 替换为实际URL
    private static final String API_URL = "http://localhost:8998/ai/helper-student/ai-chat-stream"; // 替换为实际URL

    private static final String type = "wiki";
    private static final String token = "5cbaa6965ecb41f9be5dae0c800219c8";
    private static final String imei = "86178207000055022";
    private static StringBuffer buffer = new StringBuffer();



    public static void main(String[] args) {
        List<String> questions = new ArrayList<>();
        questions.add("太阳系中最大的行星是哪一颗？");
        questions.add("中国的长城最初是为了防御哪个民族而修建的？");
        questions.add("世界上流量最大的河流是哪一条？");
        questions.add("《哈利·波特》系列的作者是谁？");
        questions.add("经典台词“生活就像一盒巧克力，你永远不知道下一块是什么味道”出自哪部电影？");
        questions.add("足球比赛中，一场比赛最多可以更换几名球员（常规时间）？");
        questions.add("披头士乐队（The Beatles）来自哪个国家？");
        questions.add("iPhone的制造商是哪家公司？");
        questions.add("现存最大的陆地动物是什么？");
        questions.add("如果今天是星期五，那么再过72小时是星期几？");
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Thread  thread = new Thread(() -> {
                AIChat(questions.get(finalI));
            });
            thread.setName("线程" + i);
            thread.start();
        }


//        AIChat();
    }

    private static void AIChat(String text) {
        Map<String, String> serverMessage = new LinkedHashMap<>();
        serverMessage.put("服务器地址", API_URL);
        serverMessage.put("设备序列号", imei);
        serverMessage.put("token", token);
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 服务器信息： " + serverMessage);
        // 准备请求参数
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("type", type);
        requestBody.put("content", text);
        requestBody.put("token", token);
        requestBody.put("imei", imei);

        // 创建RestTemplate实例并配置编码
        RestTemplate restTemplate = new RestTemplate();

        // 添加StringHttpMessageConverter并设置UTF-8编码
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringConverter.setWriteAcceptCharset(false); // 避免添加多余的Accept-Charset头
        messageConverters.add(stringConverter);

        // 添加Jackson2HttpMessageConverter（用于JSON处理）
        ObjectMapper objectMapper = new ObjectMapper();
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        messageConverters.add(jsonConverter);

        restTemplate.setMessageConverters(messageConverters);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(MediaType.parseMediaTypes("text/event-stream;charset=UTF-8"));
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【1】开始发送HTTP请求。");
        // 创建HTTP请求实体
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {

            // 发送POST请求并获取响应
            ResponseEntity<String> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【2】接收HTTP响应。");
            // 检查响应状态
            if (response.getStatusCode() == HttpStatus.OK) {
                // 处理SSE格式的响应
                System.out.println("请求成功，响应内容：");
                String[] split = Objects.requireNonNull(response.getBody()).split("\\\n\\\n");
                StringBuffer stringBuffer = new StringBuffer();
                for (String s : split) {
                    if (s != null && !s.isEmpty()) {
                        s = s.substring(5);
//                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】接收数据"+ s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        if (jsonObject.containsKey("content")) {
                            stringBuffer.append(jsonObject.getString("content"));
                        }
                    }
                }

                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】线程【"+Thread.currentThread().getName()+"】打印数据");
                System.out.println(stringBuffer);

            } else {
                System.out.println("请求失败，状态码：" + response.getStatusCode());
                System.out.println("错误信息：" + response.getBody());
            }
            System.out.println();
//            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【5】线程【"+Thread.currentThread().getName()+"】结束对话。");
        } catch (Exception e) {
            System.err.println("请求发生异常：" + e.getMessage());
            e.printStackTrace();
        }
    }
}