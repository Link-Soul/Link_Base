package com.link; /**
 * @Author zhoubinbin
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
     * 卡尔服务-asr能力的 url
     */
//    private static final String WS_URL = "ws://v2.alpha.kaerplatform.com/ai/ws/asr/stream"; // 替换为实际的 WebSocket 地址
//    private static final String WS_URL = "ws://tree.kaerplatform.com/ai/ws/asr/stream"; // 替换为实际的 WebSocket 地址
    private static final String WS_URL = "ws://localhost:8998/ai/ws/asr/stream"; // 替换为实际的 WebSocket 地址
    /**
     * 卡尔服务-asr能力需要的token
     */
    private static final String token = "955c990ddb1f4559887095223eefd326";
    private static final String imei = "45678910";

//    private static final String token = "751dc6aa32f44706b05868dd44b3fb3f";
//    private static final String imei = "321321";

    private static final String req_id = UUID.randomUUID().toString().replace("-", "");
    /**
     * 每次向服务端上传音频的大小
     */
    private static final int BUFFER_SIZE = 256 * 1024;

    //    String file = "D:\\FFOutput\\标准录音 17.mp3";
//    String type = "mp3";
//    String model = "huoshan";
//    String file = "D:\\FFOutput\\AI智能体测试.wav";
//    String file = "D:\\FFOutput\\推荐的游戏.wav";
//String file = "D:\\FFOutput\\为什么会有天气变化.mp3";
//    String file = "D:\\FFOutput\\标准录音 18.mp3";
//    String file = "D:\\FFOutput\\标准录音 17.wav";    //双声道
    String file = "D:\\FFOutput\\标准录音 17~1.wav";    //单声道。
//    String file = "D:/FFOutput/测试用例/文章.wav";
//    String file = "D:\\FFOutput\\测试.pcm";
//    String file = "D:\\FFOutput\\orange.amr";

    String type = "wav";
    String model = "turing";

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


                // 1. 发送开始的请求
                Map<String, Object> startBody = new HashMap<>();
                startBody.put("req_id", req_id);
                startBody.put("rec_status", 0);
                startBody.put("format", type);
                startBody.put("model", model);

                String startRequest = JSON.toJSONString(startBody);
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】发送开始音频识别请求 = " + startRequest);
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
//                        reqBody.put("format", type);
                        String jsonBody = JSON.toJSONString(reqBody);
                        send(jsonBody);
                        reqBody.put("audio_stream", "audio_stream");
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】发送音频识别请求 = " + reqBody);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Thread sendThread = new Thread(() -> {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Map<String, Object> endBody = new HashMap<>();
                    endBody.put("rec_status", 2);
                    endBody.put("req_id", req_id);
                    String endJson = JSON.toJSONString(endBody);
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【5】发送结束音频识别请求 = " + endBody);
                    send(endJson);
                });
                sendThread.start();


//                Thread thread  = new Thread(() -> {
//                    try {
//                        Thread.sleep(4100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    Map<String, Object> reqBody = new HashMap<>();
//                    reqBody.put("rec_status", 2);
//                    reqBody.put("req_id", req_id);
//                    String jsonBody = JSON.toJSONString(reqBody);
//                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】发送结束音频识别请求 = " + reqBody);
//                    send(jsonBody);
//                });
//                thread.start();
            }

            @Override
            public void onMessage(String message) {
                // 接收到服务端的数据
                JSONObject jsonFromServer = JSONObject.parseObject(message);
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【6】接收到服务端的消息 = " + message);
                Boolean isEnd = jsonFromServer.getBoolean("is_end");
//                if (isEnd != null && isEnd) {
//                    Map<String, Object> endBody = new HashMap<>();
//                    endBody.put("req_id", req_id);
//                    endBody.put("rec_status", 2);
//                    endBody.put("format", type);
//
//                    String endRequest = JSON.toJSONString(endBody);
//                    // todo: [4]
//                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】 接受消息 = " + endRequest);
//                    send(endRequest);
//                }
            }

            @Override
            public void onMessage(ByteBuffer bytes) {

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                // todo: [5]
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【7】 websocket连接已关闭。 ");
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