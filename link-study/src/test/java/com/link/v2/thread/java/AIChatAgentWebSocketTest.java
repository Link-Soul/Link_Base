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

public class AIChatAgentWebSocketTest {

    private static final String API_URL = "http://v2.alpha.kaerplatform.com/ai/helper-student/ai-chat-stream"; // 替换为实际URL
//    private static final String API_URL = "http://v2.alpha.kaerplatform.com/ai/helper-student/ai-chat-turing-stream"; // 替换为实际URL
//    private static final String API_URL = "http://localhost:8998/ai/helper-student/ai-chat-turing-stream"; // 替换为实际URL

    /*
        1001 讲故事  200201
        1002 好词好句 100000
        1003 作文范文   200201
        1004 ai写诗  200401
        1005 知识百科 200209
     */

    private static final String type = "essay";
    private static final String content = "给我一个100字的春游主题小作文";
    private static final String token = "326b98b901594569af006fe861afabd1";
    private static final String imei = "111111";
    private static StringBuffer buffer = new StringBuffer();


    public static void main(String[] args) {
        Map<String, String> serverMessage = new LinkedHashMap<>();
        serverMessage.put("服务器地址", API_URL);
        serverMessage.put("设备序列号", imei);
        serverMessage.put("token", token);
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 服务器信息： " + serverMessage);
        // 准备请求参数
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("type", type);
        requestBody.put("content", content);
        requestBody.put("token", token);
        requestBody.put("imei", imei);
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 发送内容： "+ content+"  。使用能力: "+type);
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
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】接收数据"+ s);
                        JSONObject jsonObject = JSONObject.parseObject(s);
                        if (jsonObject.containsKey("content")) {
                            stringBuffer.append(jsonObject.getString("content"));
                        }
                    }
                }

                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】打印数据");
                System.out.println(stringBuffer);

            } else {
                System.out.println("请求失败，状态码：" + response.getStatusCode());
                System.out.println("错误信息：" + response.getBody());
            }
            System.out.println();
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【5】结束对话。");
        } catch (Exception e) {
            System.err.println("请求发生异常：" + e.getMessage());
            e.printStackTrace();
        }
    }
}