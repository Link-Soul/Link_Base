package com.link.v2.thread.java;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author Link
 * @Date 2025/4/23 11:17
 */
public class AsrNlpTTsWebSocketTest {
//    private static final String WS_URL = "ws://v2.alpha.kaerplatform.com/ai/ws/ai-robot/stream"; // 替换为实际的 WebSocket 地址
    private static final String WS_URL = "ws://localhost:8998/ai/ws/ai-robot/stream"; // 替换为实际的 WebSocket 地址
    private static final String TEST_OUTPUT_DIR = "D:\\FFOutput\\test_voices";
    private static final String token = "1";
//    private static final String imei = "86178207000055022";
    private static final String req_id = UUID.randomUUID().toString().replace("-", "");


    private static final int BUFFER_SIZE = 1024 * 16;   // 每次读取的块大小，可根据需要调整
    private static final String format = "mp3";         // 发送的音频类型，请发送mp3
    private static final String nlp = "wiki";           // 使用的模型能力
    String file = "D:\\FFOutput\\orange.amr";


    private static Map<Integer, ByteBuffer> bufferedReaderMap = new HashMap<>();

    public static void main(String[] args) {
        Map<String, String> serverMessage = new LinkedHashMap<>();
//        serverMessage.put("服务器地址", WS_URL);
//        serverMessage.put("设备序列号", imei);
//        serverMessage.put("token", token);
//        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 服务器信息： " + serverMessage);
        for (int i = 1; i < 11; i++) {
            Thread thread = new Thread(() -> {
                try {
                    new AsrNlpTTsWebSocketTest().connectWebSocket();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.setName(String.valueOf(i));
            thread.start();
        }

    }

    /**
     * 建立WebSocket链接
     *
     * @throws URISyntaxException
     */
    public void connectWebSocket() throws URISyntaxException {
        int imei = Integer.parseInt(Thread.currentThread().getName());
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【1】线程【"+Thread.currentThread().getName()+"】开始建立websocket 连接。");
        WebSocketClient client = new WebSocketClient(new URI(WS_URL)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【2】已经建立websocket连接。");
                // base64的音频流
                // 开始流式发送音频，音频文件格式为 mp3
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead;

                    // 先发个状态为0 建立连接，并初始化
                    Map<String, Object> reqBodyone = new HashMap<>();
                    reqBodyone.put("req_id", req_id);   // 请求id，每次请求都不一样，建议使用uuid。单次请求唯一。
                    reqBodyone.put("rec_status", 0);    // 连接状态，第一次连接为0，最后一次链接为2。其余正常发送数据包为1
                    reqBodyone.put("format", format);   // 音频类型，目前支持mp3
                    reqBodyone.put("nlp", nlp);         // 对话场景

                    String init = JSON.toJSONString(reqBodyone);
                    // 开始请求第一次请求，此时rec_status为0
//                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】发送音频识别请求 = " + reqBodyone);
                    send(init);
                    System.out.println("-----> 开始进行初始化 ----->");
                    // 接下来开始正常发送音频包
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        // 截取实际读取的字节数
                        byte[] actualData = new byte[bytesRead];
                        System.arraycopy(buffer, 0, actualData, 0, bytesRead);
                        // 将当前块转换为 Base64 编码
                        String audio = Base64.getEncoder().encodeToString(actualData);
                        // 构造请求体
                        Map<String, Object> reqBody = new HashMap<>();
                        reqBody.put("rec_status", 1);
                        reqBody.put("audio_stream", audio); // 发送音频
//                        reqBody.put("format", format);        // 音频类型，目前支持mp3
                        reqBody.put("is_end", 0);
                        if (bytesRead < BUFFER_SIZE) {
                            // 这意味着 数据块已读完，也就是最后一包带音频数据的包。
                            // 完成该次发送后应该发送下方rec_status=2的结束请求，以关闭连接。
                            reqBody.put("is_end", 1);
                        }
                        String jsonBody = JSON.toJSONString(reqBody);
                        send(jsonBody);
                        if (reqBody.containsKey("audio_stream")) {
                            reqBody.put("audio_stream", "audio_stream");
                        }
                        // 示例输出
//                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 发送请求：=" + reqBody);
                    }
                    // 发送完数据时，将录音状态设置为 2，代表发送完毕。服务端完成音频数据的发送后自动关闭此次连接
                    Map<String, Object> endJson = new HashMap<>();
                    endJson.put("req_id", req_id);
                    endJson.put("rec_status", 2);
                    send(JSON.toJSONString(endJson));
                    // 示例输出
//                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】 发送音频识别结束请求 = " + endJson);
                } catch (IOException e) {
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 发送音频识别请求异常 = " + e.getMessage());
                }

            }

            /**
             * 实时接收客户端传来的消息
             * @param message The UTF-8 decoded message that was received.
             */
            @Override
            public void onMessage(String message) {
                JSONObject jsonObject = JSONObject.parseObject(message);
                if (jsonObject != null) {
                    if (jsonObject.containsKey("code") && jsonObject.getInteger("code") == 1000) {
                        // 存在audio
                        if (jsonObject.containsKey("audio")) {
                            byte[] audio = jsonObject.getBytes("audio");
                            // 如果返回数据是最后一条，则将音频数据写入文件。相应元宝中也就是完成AI对话的输出。
                            // 本套对话是单次对话，即为一问一答。目前还未支持多轮对话。所以完成对音频的返回后即自动关闭连接。
                            if (jsonObject.containsKey("is_end") && jsonObject.getBoolean("is_end")) {
                                if (bufferedReaderMap.containsKey(imei)) {
                                    ByteBuffer bytes = bufferedReaderMap.get(imei);
                                    try {
                                        String fileName = TEST_OUTPUT_DIR + "/" + UUID.randomUUID() + ".mp3";
                                        try (FileOutputStream fos = new FileOutputStream(fileName)) {
                                            fos.write(bytes.array());
                                        }
                                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【5】线程【"+Thread.currentThread().getName()+"】 服务端已完成音频发送，语音已落地，fileName = " + fileName);

                                    } catch (IOException e) {
                                        // 语音落地出现错误
                                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + "语音落地出现错误" + e.getMessage());
                                    }
                                }
                            } else {
                                // 最后一个isEnd=ture的情况，就不携带音频了。只是做一个终止标志。
                                // 不是最后一条则仅接受。元宝可以一直拿音频数据然后播放。
                                if (bufferedReaderMap.containsKey(imei)) {
                                    ByteBuffer append = append(bufferedReaderMap.get(imei), audio);
                                    bufferedReaderMap.put(imei, append);
                                } else {
                                    bufferedReaderMap.put(imei, ByteBuffer.wrap(audio));
                                }
                                if (jsonObject.containsKey("audio")) {
                                    jsonObject.put("audio", "audio");
                                }
//                                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 接收到服务端的消息 = " + jsonObject);

                            }
                        }
                    }else {
                        System.out.println("出现错误，异常结果为： " + jsonObject);
                    }
                } else {
                    // 出现错误，输出错误信息
                    System.out.println("出现错误，异常结果为： " + jsonObject);
                }


            }

            /**
             * 结束后的操作
             */
            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【6】 websocket连接已关闭。 ");
            }

            /**
             * 出现错误的操作
             */
            @Override
            public void onError(Exception ex) {
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " websocket连接出现异常：" + ex.getMessage());

            }
        };
        System.out.println("-----> 开始建立连接 ----->");
        // 设置请求头
        client.addHeader("IMEI", String.valueOf(imei));
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