package com.link; /**
 * @Author Link
 * @Date 2025/4/23 11:17
 */

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jdk.nashorn.internal.ir.CallNode;
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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TTS模拟测试类
 */
public class TTSWebSockV2Test {
//    private static final String WS_URL = "ws://v2.alpha.kaerplatform.com/ai/ws/v2/tts"; // 替换为实际的 WebSocket 地址
        private static final String WS_URL = "ws://localhost:8998/ai/ws/v2/tts"; // 替换为实际的 WebSocket 地址
//private static final String WS_URL = "ws://tree.kaerplatform.com/ai/ws/v2/tts";
//        private static final String WS_URL = "ws://localhost:8998/ai/ws/v2/tts"; // 替换为实际的 WebSocket 地址
    private static final String TEST_OUTPUT_DIR = "D:\\FFOutput\\test_voices";
    private static final String token = "955c990ddb1f4559887095223eefd326";
    private static final String imei = "45678910";

//    private static final String token = "751dc6aa32f44706b05868dd44b3fb3f";
//    private static final String imei = "321321";

    private static final String format = "mp3";


    private static final String model = "turing";

    private static String fileName = TEST_OUTPUT_DIR + "/" + UUID.randomUUID().toString().replace("-", "") + "." + format;
    private static FileOutputStream fos = null;

    {
        try {
            fos = new FileOutputStream(fileName);
        } catch (IOException e) {
            System.out.println("-------> 创建文件失败！");
            e.printStackTrace();
        }
    }

    private AtomicInteger totalTimes = new AtomicInteger(0);


    public static void main(String[] args) {
        Map<String, String> serverMessage = new LinkedHashMap<>();
        serverMessage.put("服务器地址", WS_URL);
        serverMessage.put("设备序列号", imei);
        serverMessage.put("token", token);
        serverMessage.put("model", model);
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 服务器信息： " + serverMessage);
        try {
            new TTSWebSockV2Test().connectWebSocket();
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
                //TODO [1]初始化
                Map<String, Object> initBody = new HashMap<>();
                initBody.put("format", format);
                initBody.put("rec_status", 0);
                initBody.put("model", model);
                send(JSON.toJSONString(initBody));

                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】发送服务初始化请求");

                // 构造请求体

                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(200);



                        snedText("好难受，最近好累啊");



                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                thread.start();


            }

            private void snedText(String content) {
                Map<String, Object> reqBody = new HashMap<>();
                reqBody.put("content", content);
                reqBody.put("rec_status", 1);
                String jsonBody = JSON.toJSONString(reqBody);
                send(jsonBody);
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】发送TTS请求 = " + jsonBody);
                totalTimes.incrementAndGet();
            }

            private void sendClose() throws IOException {

                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【5】语音已落地 = " + fileName);

                Map<String, Object> endBody = new HashMap<>();
                endBody.put("rec_status", 2);
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【6】发送TTS结束请求");
                send(JSON.toJSONString(endBody));
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
//                        byteBuffer = append(byteBuffer, decode);
                        fos.write(decode);
                        if (jsonObject.containsKey("sequence") && jsonObject.getInteger("sequence") < 0) {
                            totalTimes.decrementAndGet();
                        }
                        if (totalTimes.get() == 0) {
                            sendClose();
                        }

                    } else {
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 接收到服务端的其他消息 = " + jsonObject);
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
/*                try {
                    String reqId = (String) JSON.parseObject(new String(bytes.array(), "UTF-8"), Map.class).get("req_id");
                    String fileName = TEST_OUTPUT_DIR + "/" + reqId + ".mp3";
                    try (FileOutputStream fos = new FileOutputStream(fileName)) {
                        fos.write(bytes.array());
                    }
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】接收到服务端的音频数据 = ");
                } catch (IOException e) {
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 发送音频出现异常: " + e.getMessage());
                }*/
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        throw new RuntimeException("文件流关闭异常", e);
                    }
                }
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【7】 websocket连接已关闭。 ");
            }

            @Override
            public void onError(Exception ex) {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        throw new RuntimeException("文件流关闭异常", e);
                    }
                }
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " websocket连接出现问题。 " + ex.getMessage());
            }
        };

        // 设置请求头
        client.addHeader("IMEI", imei);
        client.addHeader("Authorization", token);
        client.connect();
    }

    /**
     * ByteBuffer数据追加方法
     *
     * @param original
     * @param newData
     * @return
     */
    public static ByteBuffer append(ByteBuffer original, byte[] newData) {
        // 确保新容量足够容纳原数据和新数据
        int newCapacity = original.capacity() + newData.length;
        // 创建新的ByteBuffer
        ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
        // 保存原Buffer的position和limit
        int oldPosition = original.position();
        original.rewind(); // 将position重置为0以便复制
        // 复制原数据到新Buffer
        newBuffer.put(original);
        // 追加新数据
        newBuffer.put(newData);
        // 恢复原Buffer的position（可选）
        original.position(oldPosition);
        // 设置新Buffer的position为数据末尾，准备读取
        newBuffer.flip();
        return newBuffer;
    }
}