package com.link.v2.nlp;

import com.alibaba.fastjson2.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AIChatAgentWebSocketTest {

    private static final String API_URL = "http://v2.alpha.kaerplatform.com/ai/helper-student/ai-chat-stream";
//    private static final String API_URL = "https://tree.kaerplatform.com/ai/helper-student/ai-chat-stream";
//    private static final String API_URL = "http://localhost:8998/ai/helper-student/ai-chat-stream";
    private static final String type = "wiki";
//    private static final String type = "ai";
//    private static final String type = "story";
//    private static final String type = "essay";
//    private static final String type = "sentence";
//    private static final String type = "poem";
    private static final String content = "周杰伦对象是谁";
    private static final String token = "955c990ddb1f4559887095223eefd326";
    private static final String imei = "45678910";

    public static void main(String[] args) {
        Map<String, String> serverMessage = new HashMap<>();
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
        requestBody.put("device_type", "1049");

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 发送内容： " + content + "  。使用能力: " + type);

        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "text/event-stream");
            connection.setDoOutput(true);

            // 发送请求体
            String requestBodyStr = JSONObject.toJSONString(requestBody);
            connection.getOutputStream().write(requestBodyStr.getBytes(StandardCharsets.UTF_8));

            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【1】开始发送HTTP请求。");

            // 读取流式响应
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("data:")) {
                    String jsonStr = line.substring(5).trim();
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【2】接收数据: " + jsonStr);

                    JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                    if (jsonObject.containsKey("content")) {
//                        System.out.print(jsonObject.getString("content"));
                        response.append(jsonObject.getString("content"));
                    }
                }
            }

            System.out.println("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】流式传输完成。最终结果："+  response);

            connection.disconnect();
        } catch (Exception e) {
            System.err.println("请求发生异常：" + e.getMessage());
            e.printStackTrace();
        }
    }
}