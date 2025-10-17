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
public class TTSWebSockV2Test {
//    private static final String WS_URL = "ws://v2.alpha.kaerplatform.com/ai/ws/v2/tts"; // 替换为实际的 WebSocket 地址
    private static final String WS_URL = "ws://localhost:8998/ai/ws/v2/tts"; // 替换为实际的 WebSocket 地址
    private static final String TEST_OUTPUT_DIR = "D:\\FFOutput\\test_voices";
    private static final String token = "9aa75a6640394a8ab47f11305f427fad";
    private static final String imei = "123123123";
    private static final String format = "mp3";

    //    private static ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private static Map<String, ByteBuffer> byteBufferMap = new HashMap<>();

    private static String content = "这是测试";

    private static final String model = "turing";

    public static void main(String[] args) {


        for (int i = 1; i < 3; i++) {
            Thread thread = new Thread(() -> {
                Map<String, String> serverMessage = new LinkedHashMap<>();
                serverMessage.put("服务器地址", WS_URL);
                String imei = Thread.currentThread().getName();
                serverMessage.put("设备序列号", imei);
                serverMessage.put("token", "1");
                serverMessage.put("model", model);
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 服务器信息： " + serverMessage);
                try {
                    // todo: [1]
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + "线程【" + Thread.currentThread().getName() + "】开始建立websocket 连接。");
                    new TTSWebSockV2Test().connectWebSocket();
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
        String token = "1";
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【1】线程 " + imei + " 开始建立websocket 连接。");
        WebSocketClient client = new WebSocketClient(new URI(WS_URL)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【2】线程 " + imei + " 已经建立websocket连接。");
                //TODO [1]初始化
                Map<String, Object> initBody = new HashMap<>();
                initBody.put("format", format);
                initBody.put("rec_status", 0);
                initBody.put("model", model);
                send(JSON.toJSONString(initBody));
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】线程 " + imei + " 发送服务初始化请求");

//                String[] split = content.split("[,，。.!！？?]");
//                for (String text : split) {
//                    // 构造请求体
//                    Map<String, Object> reqBody = new HashMap<>();
//                    reqBody.put("content", text);
//                    reqBody.put("rec_status", 1);
//                    String jsonBody = JSON.toJSONString(reqBody);
//                    send(jsonBody);
//                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】发送TTS请求 = " + jsonBody);
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }


                // 构造请求体

                Map<String, Object> reqBody = new HashMap<>();
                reqBody.put("content", content);
                reqBody.put("rec_status", 1);
                String jsonBody = JSON.toJSONString(reqBody);
                send(jsonBody);
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】发送TTS请求 = " + jsonBody);


//                // 第二包数据
//                Thread thread  = new Thread(() -> {
//                    totalText.split("，");
//                    try {
//                        Thread.sleep(5000);
//                        reqBody.put("content", "测试第二段");
//                        String jsonString = JSON.toJSONString(reqBody);
//                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】发送TTS请求 = " + jsonString);
//                        send(jsonString);
//                    } catch (Exception e) {}
//
//
//
//                  });
//                thread.start();
// 关闭


            }

            @Override
            public void onMessage(String message) {

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject = JSONObject.parseObject(message);
                    if (jsonObject.containsKey("audio")) {
                        String audio = jsonObject.getString("audio");
                        jsonObject.put("audio", "audio_steam");


                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】接收到服务端的TTS返回结果 = " + jsonObject);
                        byte[] decode = Base64.getDecoder().decode(audio);
                        ByteBuffer byteBuffer = byteBufferMap.get(imei);
                        if (byteBuffer == null) {
                            ByteBuffer newBuffer = ByteBuffer.allocate(1024);
                            newBuffer = append(newBuffer, decode);
                            byteBufferMap.put(imei, newBuffer);
                        } else {
                            byteBuffer = append(byteBuffer, decode);
                            byteBufferMap.put(imei, byteBuffer);
                        }

                        // 停止
                        if (jsonObject.containsKey("sequence") && jsonObject.getInteger("sequence") < 0) {
                            Map<String, Object> endBody = new HashMap<>();
                            endBody.put("rec_status", 2);
                            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】发送TTS结束请求");
                            send(JSON.toJSONString(endBody));

                            //处理音频
                            try {
                                ByteBuffer byteBufferEnd = byteBufferMap.get(imei);
                                String fileName = TEST_OUTPUT_DIR + "/" + UUID.randomUUID().toString().replace("-", "") + "." + format;
                                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                                    byte[] byteArray = new byte[byteBufferEnd.remaining()]; // 创建与剩余数据大小相同的数组
                                    byteBufferEnd.get(byteArray); // 将 ByteBuffer 中的数据复制到 byte[]
                                    fos.write(byteArray);
                                    byteBufferEnd.clear();
                                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【5】语音已落地 = " + fileName);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

//                        if (jsonObject.getInteger("sequence") > 0 ){
//                            // todo 在此处存数据到 byteBuffer ，待所有数据接收完毕，统一写入文件
//                            // 存储
//                            byteBuffer = append(byteBuffer, decode);
//                        }else {
//                            String fileName = TEST_OUTPUT_DIR + "/" + "turing"+ "." + format;
//                            try (FileOutputStream fos = new FileOutputStream(fileName)) {
//                                byte[] byteArray = new byte[byteBuffer.remaining()]; // 创建与剩余数据大小相同的数组
//                                byteBuffer.get(byteArray); // 将 ByteBuffer 中的数据复制到 byte[]
//                                fos.write(byteArray);
//                                byteBuffer.clear();
//                                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【5】语音已落地 = " + fileName);
//                            }
//                        }

                    } else {
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】接收到服务端的其他消息 = " + jsonObject);
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