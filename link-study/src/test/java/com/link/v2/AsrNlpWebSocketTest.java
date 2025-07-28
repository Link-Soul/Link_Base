package com.link.v2;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.link.ai.NlpEnum;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author zhoubinbin
 * @Date 2025/4/23 11:17
 */
public class AsrNlpWebSocketTest {

    private static final String WS_URL = "ws://v2.alpha.kaerplatform.com/ai/ws/asr-nlp/stream"; // 替换为实际的 WebSocket 地址
//    private static final String WS_URL = "ws://localhost:8998/ai/ws/asr-nlp/stream"; // 替换为实际的 WebSocket 地址
    private static final String token = "3e6f864d702c43dfb141ab73293b9264";
    private static final String imei = "861782070000557";
    private static final String req_id = UUID.randomUUID().toString().replace("-", "");

    private static StringBuilder stringBuilder = new StringBuilder();

    int BUFFER_SIZE = 1024 * 32; // 每次读取的块大小，可根据需要调整
//    String file = "D:\\FFOutput\\orange.amr";
//    String file = "D:\\FFOutput\\提醒我下午三点开会.mp3";
//    String file = "D:\\FFOutput\\明天21点.mp3";
    String file = "D:\\FFOutput\\标准录音 17.wav";
//    String file = "D:\\FFOutput\\test.mp3";

    String type = "wav";
    private static final Integer nlp = NlpEnum.AI_ALARM.getNlp(); // 使用的模型能力
    private static final Integer sample_rate = 8000;            // 采样率
    private static final Integer bits = 32;                      // 音频采样点位数


    public static void main(String[] args) {
        Map<String, String> serverMessage = new LinkedHashMap<>();
        serverMessage.put("服务器地址", WS_URL);
        serverMessage.put("设备序列号", imei);
        serverMessage.put("token", token);
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 服务器信息： " + serverMessage);
        try {
            new AsrNlpWebSocketTest().connectWebSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 建立WebSocket链接
     *
     * @throws URISyntaxException
     */
    public void connectWebSocket() throws URISyntaxException {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【1】开始建立websocket 连接。");
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
                    reqBodyone.put("rec_status", 0);             // 连接状态，第一次连接为0，最后一次链接为2。其余正常发送数据包为1
                    reqBodyone.put("format", type);              // 音频类型，目前支持mp3、amr、wav
                    reqBodyone.put("nlp", nlp);                  // 使用的模型能力
//                    reqBodyone.put("sample_rate", sample_rate);  // 音频采样率 非必填
//                    reqBodyone.put("bits", bits);                // 音频采样点位数 非必填
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【3】发送音频识别请求 = " + reqBodyone);
                    String init = JSON.toJSONString(reqBodyone);
                    // 开始请求第一次请求，此时rec_status为0
                    send(init);
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
                        reqBody.put("audio_stream", audio);
                        reqBody.put("is_end", 0);
                        if (bytesRead < BUFFER_SIZE) {
                            // 这意味着 数据块已读完，也就是最后一包带音频数据的包。
                            // 完成该次发送后应该发送下方rec_status=2的结束请求，以关闭连接。
                            reqBody.put("is_end", 1);
                        }
                        String jsonBody = JSON.toJSONString(reqBody);
                        send(jsonBody);


                        reqBody.remove("audio_stream");
                        reqBody.put("audio_len", actualData.length);
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 发送请求：=" + reqBody);
                    }
                    // 发送完数据时，将录音状态设置为 2，代表发送完毕。服务端完成音频数据的发送后自动关闭此次连接
                    Map<String, Object> endJson = new HashMap<>();
                    endJson.put("req_id", req_id);  //应为接收到的req_id
                    endJson.put("rec_status", 2);
                    send(JSON.toJSONString(endJson));
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【4】 发送音频识别结束请求 = " + endJson);
                } catch (IOException e) {
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 发送音频出现异常: " + e.getMessage());
                }
            }

            /**
             * 实时接收客户端传来的消息
             * @param message The UTF-8 decoded message that was received.
             */
            @Override
            public void onMessage(String message) {
                // 示例输出
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 接收到服务端的消息 = " + message);
                JSONObject jsonObject = JSONObject.parseObject(message);
                if (jsonObject.getInteger("code") == 1000) {
                    if (jsonObject.containsKey("sequence") && jsonObject.getInteger("sequence") < 0) {
                        stringBuilder.append(jsonObject.getString("text"));
                    }
                    if (jsonObject.containsKey("auth") && jsonObject.getString("auth").equals("auth")) {
                        System.out.println("-----> 初始化完成 ----->");
                    }
                    if (jsonObject.containsKey("is_end") && jsonObject.getBoolean("is_end") && jsonObject.containsKey("sequence") && jsonObject.getInteger("sequence") < 0) {
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss.SSS")) + " 【5】asr结果输出结束，输出内容为： " + stringBuilder.toString());
                    }
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
             * @param ex The exception causing this error
             */
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