package com.link.v2.thread.java; /**
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
//    private static final String WS_URL = "ws://v2.alpha.kaerplatform.com/ai/ws/helper-student/tts";
    private static final String WS_URL = "ws://localhost:8998/ai/ws/helper-student/tts";
    private static final String TEST_OUTPUT_DIR = "D:\\FFOutput\\test_voices";
    private static final String token = "1";
    private static final String format = "mp3";
    /**
     * tts 转义的文本
     */
    private static final String content = "这是一段测试音频";

    private static String fileName = TEST_OUTPUT_DIR + "/" + UUID.randomUUID().toString().replace("-", "") + "." + format;

    FileOutputStream fos = null;

    public static void main(String[] args) {
        for (int i = 1; i < 3; i++) {
            Thread thread = new Thread(() -> {
                try {
                    // todo: [1]
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【1】线程【"+Thread.currentThread().getName()+"】开始建立websocket 连接。");
                    new TTSWebSockTest().connectWebSocket();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            });
            thread.setName(String.valueOf(i));
            thread.start();
        }
    }

    public void connectWebSocket() throws URISyntaxException {
        String imei = Thread.currentThread().getName();

        // 创建处理音频为的文件流
        try {
            fos = new FileOutputStream(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        WebSocketClient client = new WebSocketClient(new URI(WS_URL)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                // todo: [2]
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【2】已经建立websocket连接。");

                // 构造请求体
                Map<String, Object> reqBody = new HashMap<>();
                reqBody.put("imei", imei);
                reqBody.put("token", token);
                reqBody.put("content", content);
                reqBody.put("format", "mp3");

                String jsonBody = JSON.toJSONString(reqBody);
                send(jsonBody);
                // todo: [3]
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】发送音频合成请求 = " + jsonBody);

            }

            @Override
            public void onMessage(String message) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = JSONObject.parseObject(message);
                    String audio = jsonObject.getString("audio");

                    jsonObject.remove("audio");
                    jsonObject.put("audio_len", audio.length());
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】线程【"+Thread.currentThread().getName()+"】接收到服务端的消息 = " + jsonObject);
                    // 音频数据罗盘
                    byte[] decode = Base64.getDecoder().decode(audio);
                    fos.write(decode);
                } catch (NullPointerException ne) {
//                    System.out.println("空指针异常" + ne);
//                    System.out.println("收到的服务器消息：" + jsonObject);
                } catch (Exception e) {
                    System.out.println("异常" + e.getMessage());
                }
                if ((Integer) jsonObject.get("code") != 1000) {
                    System.out.println("收到的服务器消息：" + jsonObject);
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
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【5】 音频已落地。 ");
                    // 完成落地后自动关闭连接
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

                // todo: [5]
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【6】 websocket连接已关闭。 ");
            }

            @Override
            public void onError(Exception ex) {
                System.err.println("WebSocket 错误：" + ex.getMessage());
            }
        };

        // 设置请求头
        client.addHeader("IMEI", imei);
        client.addHeader("Authorization", token);
        client.connect();
    }
}