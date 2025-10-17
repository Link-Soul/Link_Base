package com.link.v2.thread.java; /**
 * @Author Link
 * @Date 2025/4/23 11:17
 */

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ASRWebSockTest {
    /**
     * asr能力的 url
     */
    private static final String WS_URL = "ws://v2.alpha.kaerplatform.com/ai/ws/asr/stream";
    /**
     * asr能力需要的token
     */
    private static final String token = "52b811e1026242e5939c23b97e2f00bf";
    private static final String imei = "86178207000055022";

    private static final String req_id = UUID.randomUUID().toString().replace("-", "");
    /**
     * 每次向服务端上传音频的大小
     */
    private static final int BUFFER_SIZE = 128 * 1024;

    String file = "D:\\FFOutput\\标准录音 17.wav";
    String type = "wav";

    public static void main(String[] args) {
        Map<String, String> serverMessage = new LinkedHashMap<>();
        serverMessage.put("服务器地址", WS_URL);
        serverMessage.put("设备序列号", imei);
        serverMessage.put("token", token);
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 服务器信息： " + serverMessage);
        try {
            new ASRWebSockTest().connectWebSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void connectWebSocket() throws URISyntaxException {

        // todo: [1]
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【1】开始建立websocket 连接。");
        WebSocketClient client = new WebSocketClient(new URI(WS_URL)) {
            @SneakyThrows
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                // todo: [2]
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【2】已经建立websocket连接。");


                // 1. 发送请求头
                Map<String, Object> startBody = new HashMap<>();
                startBody.put("req_id", req_id);
                startBody.put("rec_status", 0);
                startBody.put("format", type);

                String startRequest = JSON.toJSONString(startBody);
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】发送音频识别请求 = " + startRequest);
                this.send(startRequest);


                // 2. 发送音频信息
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead;
                    int recStatus = 1; // 初始录音状态
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        // 构造请求体
                        Map<String, Object> reqBody = new HashMap<>();

                        if (bytesRead < BUFFER_SIZE) {
                            reqBody.put("is_end", 1);
                        } else {
                            reqBody.put("is_end", 0);
                        }

                        // 截取实际读取的字节数
                        byte[] actualData = new byte[bytesRead];
                        System.arraycopy(buffer, 0, actualData, 0, bytesRead);
                        // 将当前块转换为 Base64 编码
                        String audio = Base64.getEncoder().encodeToString(actualData);

                        reqBody.put("req_id", req_id);
                        reqBody.put("rec_status", 1);
                        reqBody.put("audio_stream", audio);
                        reqBody.put("format", type);
                        String jsonBody = JSON.toJSONString(reqBody);
                        send(jsonBody);
                        reqBody.remove("audio_stream");
                        reqBody.put("audio_len", audio.length());
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 发送请求：=" + reqBody);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String message) {
                // 接收到服务端的数据
                JSONObject jsonFromServer = JSONObject.parseObject(message);
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 接收到服务端的消息 = " + message);
                Boolean isEnd = jsonFromServer.getBoolean("is_end");
                if (isEnd != null && isEnd) {
                    Map<String, Object> endBody = new HashMap<>();
                    endBody.put("req_id", req_id);
                    endBody.put("rec_status", 2);
                    endBody.put("format", type);

                    String endRequest = JSON.toJSONString(endBody);
                    // todo: [4]
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】 发送音频识别结束请求 = " + endRequest);
                    send(endRequest);
                }
            }

            @Override
            public void onMessage(ByteBuffer bytes) {

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                // todo: [5]
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【5】 websocket连接已关闭。 ");
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

    public static String audioToBase64(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            byte[] audioBytes = new byte[(int) new File(filePath).length()];
            fis.read(audioBytes);
            return Base64.getEncoder().encodeToString(audioBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}