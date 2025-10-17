package com.link; /**
 * @Author Link
 * @Date 2025/4/23 11:17
 */

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * TTS模拟测试类
 */
public class TTSWebSockTest {
//    private static final String WS_URL = "ws://v2.alpha.kaerplatform.com/ai/ws/helper-student/tts"; // 替换为实际的 WebSocket 地址
private static final String WS_URL = "ws://localhost:8998/ai/ws/helper-student/tts"; // 替换为实际的 WebSocket 地址
    private static final String TEST_OUTPUT_DIR = "D:\\FFOutput\\test_voices";
    private static final String token = "955c990ddb1f4559887095223eefd326";
    private static final String imei = "45678910";
    private static final String format = "mp3";

    public static void main(String[] args) {
        Map<String, String> serverMessage = new LinkedHashMap<>();
        serverMessage.put("服务器地址", WS_URL);
        serverMessage.put("设备序列号", imei);
        serverMessage.put("token", token);
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 服务器信息： " + serverMessage);
        try {
            new TTSWebSockTest().connectWebSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void connectWebSocket() throws URISyntaxException {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【1】开始建立websocket 连接。");
        WebSocketClient client = new WebSocketClient(new URI(WS_URL)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【2】已经建立websocket连接。");
                // 构造请求体
                Map<String, Object> reqBody = new HashMap<>();
                reqBody.put("content", "这是一段测试音频");
                reqBody.put("format", format);
                String jsonBody = JSON.toJSONString(reqBody);
                send(jsonBody);
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】发送TTS请求 = " + jsonBody);
            }

            @Override
            public void onMessage(String message) {
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject = JSONObject.parseObject(message);
                    if (jsonObject.containsKey("audio")) {
                        String audio = jsonObject.getString("audio");
                        jsonObject.put("audio", "audio_steam");
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】接收到服务端的消息 = " + jsonObject);
                        byte[] decode = Base64.getDecoder().decode(audio);
                        String fileName = TEST_OUTPUT_DIR + "/" + UUID.randomUUID().toString().replace("-", "") + "." + format;
                        try (FileOutputStream fos = new FileOutputStream(fileName)) {
                            fos.write(decode);
                            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【5】语音已落地 = " + fileName);
                        }
                    }
                } catch (NullPointerException ne) {
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 发送音频出现空指针异常: " + ne.getMessage());
                } catch (Exception e) {
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 发送音频出现异常: " + e.getMessage());
                }
            }

            /**
             * 收到二进制消息，返回音频文件
             * @param bytes 二进制消息
             */
            @Override
            public void onMessage(ByteBuffer bytes) {
                try {
                    String reqId = (String) JSON.parseObject(new String(bytes.array(), "UTF-8"), Map.class).get("req_id");
                    String fileName = TEST_OUTPUT_DIR + "/" + reqId + ".mp3";
                    try (FileOutputStream fos = new FileOutputStream(fileName)) {
                        fos.write(bytes.array());
                    }
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】接收到服务端的音频数据 = ");
                } catch (IOException e) {
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 发送音频出现异常: " + e.getMessage());
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【6】 websocket连接已关闭。 ");
            }

            @Override
            public void onError(Exception ex) {
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " websocket连接出现问题。 " + ex.getMessage());
            }
        };

        // 设置请求头
        client.addHeader("IMEI", imei);
        client.addHeader("Authorization", token);
        client.connect();
    }
}