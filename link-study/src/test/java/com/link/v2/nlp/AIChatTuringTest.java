package com.link.v2.nlp;

import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AIChatTuringTest {

        private static final String API_URL = "http://v2.alpha.kaerplatform.com/ai/helper-student/ai-chat-turing-stream";
//    private static final String API_URL = "https://tree.kaerplatform.com/ai/helper-student/ai-chat-turing-stream";
    //    private static final String API_URL = "http://localhost:8998/ai/helper-student/ai-chat-turing-stream";
//    private static final String type = "200209";
    private static final String type = "200201";
    private static final String content = "介绍一下法伦宫。";
    private static final String token = "955c990ddb1f4559887095223eefd326";
    private static final String imei = "45678910";

    /*
        能力列表：
        100000	儿童聊天        100302	知识库     200205	十万个为什么      200209	维基百科        200301	动物叫声
        200302	大自然声音       200303	乐器声音        1000732	单位换算        200101	歌曲点播        200702	日期查询
        200702	时间查询        200201	故事点播        200401	诗词背诵        201401	天气查询        201711	中英互译
        200710	闹钟      900110	电量查询        900101	休眠      900110	屏幕亮度        200701	跳舞      201601	打开应用
     */

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

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 发送内容： " + content + "  。使用能力: " + type);

        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json"); // 改为普通 JSON 请求
            connection.setDoOutput(true);

            // 发送请求体
            String requestBodyStr = JSONObject.toJSONString(requestBody);
            connection.getOutputStream().write(requestBodyStr.getBytes(StandardCharsets.UTF_8));

            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【1】开始发送HTTP请求。");

            // 读取完整响应（非流式）
            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }

            // 解析 JSON 响应
            String responseStr = responseBuilder.toString();
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【2】完整响应: " + responseStr);

            System.out.println("\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】请求完成。");

            connection.disconnect();
        } catch (Exception e) {
            System.err.println("请求发生异常：" + e.getMessage());
            e.printStackTrace();
        }
    }
}