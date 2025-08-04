package com.link.v2.truing;

import com.alibaba.fastjson.JSONObject;

import com.link.turing.utils.FileUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * ws协议请求tts协议
 */
public class AsrWsDemo {

    // 地址
    public static final String hostUrl = "ws://ws-api.turingapi.com/api/v2";
    // 均到biz的机器人信息页面获取
    public static final String apiSecret = "11F******oC";
    public static final String apiKey = "f2183c306a054ebeacc0180154d1c4f3";
    public static final String deviceId = "12332323";

    public static boolean wsCloseFlag = false;
    public static String uuid= UUID.randomUUID().toString().replaceAll("-","");
//    public static String filePath= "D:/FFOutput/测试.pcm";
    public static String filePath= "D:/FFOutput/标准录音 17.wav";


    public static void main(String[] args) throws Exception {
        websocketWork(hostUrl);
    }

    // Websocket方法
    public static void websocketWork(String wsUrl) {
        try {
            URI uri = new URI(wsUrl);
            WebSocketClient webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("ws建立连接成功...");
                }

                @Override
                public void onMessage(String text) {
                    System.out.println("服务器返回: " + text);
                    try {
                        JSONObject response = JSONObject.parseObject(text);
                        if (response.getInteger("code") == 210) {
                            System.out.println("服务器已准备好接收音频数据");
                        } else if (response.getInteger("code") == 200) {
                            String asrValue = response.getJSONObject("asrResponse").getString("value");
                            System.out.println("识别结果: " + asrValue);
                        }
                    } catch (Exception e) {
                        System.err.println("JSON 解析失败: " + e.getMessage());
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("ws链接已关闭，本次请求完成...");
                    wsCloseFlag = true;
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("发生错误 " + e.getMessage());
                    wsCloseFlag = true;
                }
            };
            // 建立连接
            webSocketClient.connect();
            while (!webSocketClient.getReadyState().equals(org.java_websocket.enums.ReadyState.OPEN)) {
                System.out.println("正在连接...");
                Thread.sleep(100);
            }

            MyThread webSocketThread = new MyThread(webSocketClient, requestStart(),requestByte(),requestEnd());
            webSocketThread.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
//            System.out.println(e);
        }


    }

    //二进制数据传输开始标志
    public static String requestStart() {
        System.out.println("uuid--->"+uuid);
        String requestJson = "{" +
                "    \"key\": \""+apiKey+"\"," +
                "    \"timestamp\": \""+System.currentTimeMillis()+"\"," +
                "    \"data\": {" +
                "        \"deviceId\": \""+deviceId+"\"," +
                "        \"requestType\": [" +
                "            0" +
                "        ]," +
                "        \"nlpRequest\": {" +
                "            \"content\": [" +
                "                {" +
                "                    \"data\": \""+uuid+"\"," +
                "                    \"type\": 2" +
                "                }" +
                "            ]" +
                "        }," +
                "        \"asrRequest\": {" +
                "            \"asrFormatEnum\": 0," +
                "            \"asrLanguageEnum\": 0," +
                "            \"asrRateEnum\": 1," +
                "            \"enableITN\": true," +
                "            \"enablePunctuation\": true" +
                "        }," +
                "        \"binarysState\": {" +
                "            \"openBinarysId\": \""+uuid+"\"" +
                "        }" +
                "    }" +
                "}";
        return requestJson;
    }

    //传输二进制数据
    public static byte[] requestByte() throws IOException {
//        URL resourceUrl = AsrWsDemo.class.getClassLoader().getResource(filePath);
//        String resourcePath = resourceUrl.getPath();
//        byte [] sendByte= FileUtil.read(resourcePath);
//        return sendByte;

        return Files.readAllBytes(Paths.get(filePath));
    }


    //二进制数据传输结束
    public static String requestEnd() {
        String requestJson = "{" +
                "    \"key\": \""+apiKey+"\"," +
                "    \"timestamp\": \""+System.currentTimeMillis()+"\"," +
                "    \"data\": {" +
                "        \"binarysState\": {" +
                "            \"completeBinarysId\": \""+uuid+"\"" +
                "        }" +
                "    }" +
                "}";
        return requestJson;
    }


    // 线程来发送音频与参数
    static class MyThread extends Thread {
        WebSocketClient webSocketClient;
        String sendStartMessage;
        byte[] sendByte;
        String sendEndMessage;

        public MyThread(WebSocketClient webSocketClient, String sendStartMessage,
                        byte [] sendByte,String sendEndMessage) {
            this.webSocketClient = webSocketClient;
            this.sendStartMessage = sendStartMessage;
            this.sendByte=sendByte;
            this.sendEndMessage=sendEndMessage;
        }

        public void run() {
            try {
                //1，发送二进制开始数据
                webSocketClient.send(sendStartMessage);
                Thread.sleep(200); // 等待服务器准备
                //2，发送二进制音频数据
                webSocketClient.send(sendByte);
                //3，发送二进制结束数据
                webSocketClient.send(sendEndMessage);
                //4，等待服务端返回完毕后关闭
                while (!wsCloseFlag) {
                    Thread.sleep(200);
                }
                webSocketClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
