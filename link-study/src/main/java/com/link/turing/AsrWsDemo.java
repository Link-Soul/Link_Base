package com.link.turing;

import com.alibaba.fastjson.JSONObject;
import com.link.turing.utils.FileUtil;

import org.apache.ibatis.javassist.bytecode.ByteArray;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

/**
 * ws协议请求tts协议
 */
public class AsrWsDemo {

    // 地址
    public static final String hostUrl = "ws://ws-api.turingapi.com/api/v2";
    // 均到biz的机器人信息页面获取
    public static final String apiSecret = "11F******oC";
    public static final String apiKey = "bbd7016a48f444f6a7d9c96c66b2de6e";
    public static final String deviceId = "ai11223344556677";

    public static boolean wsCloseFlag = false;
    public static String uuid= UUID.randomUUID().toString().replaceAll("-","");
//    public static String filePath= "D:\\FFOutput\\n4an0-wzggy.opus";
    public static String filePath= "D:\\FFOutput\\标准录音 17.mp3";
    private static final int BUFFER_SIZE = 1024 * 16; // 每次读取的块大小，可根据需要调整

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
                    try {
                        //1，发送二进制开始数据
                        send(requestStart());
                        //2，发送二进制音频数据
                        send(requestByte().toString());
                        //3，发送二进制结束数据
//                        send(requestEnd());
                        //4，等待服务端返回完毕后关闭
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onMessage(String text) {
                    //服务器相应内容
                    System.out.println(text);
                    JSONObject response=JSONObject.parseObject(text);
                    if (response.getInteger("code")==200
                            && response.getJSONObject("asrResponse").getString("binarysId").equals(uuid)){
                        System.out.println("binarysId = " + response.getJSONObject("asrResponse").getString("binarysId"));
                        String asrValue=response.getJSONObject("asrResponse").getString("value");
                        System.out.println("asrValue---->"+asrValue);
                    }
                    if (response.getBoolean("done")){
                        // 关闭连接
                        close();
                    }
                }


                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("ws链接已关闭，本次请求完成...");
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("发生错误 " + e.getMessage());
                }
            };
            // 建立连接
            webSocketClient.connect();


//            MyThread webSocketThread = new MyThread(webSocketClient, requestStart(),requestByte(),requestEnd());
//            webSocketThread.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    //二进制数据传输开始标志
    public static String requestStart() {
        System.out.println("uuid--->"+uuid);
        String requestJson = "{\n" +
                "    \"key\": \""+apiKey+"\",\n" +
                "    \"timestamp\": \""+System.currentTimeMillis()+"\",\n" +
                "    \"data\": {\n" +
                "        \"deviceId\": \""+deviceId+"\",\n" +
                "        \"requestType\": [\n" +
                "            0\n" +
                "        ],\n" +
                "        \"nlpRequest\": {\n" +
                "            \"content\": [\n" +
                "                {\n" +
                "                    \"data\": \""+uuid+"\",\n" +
                "                    \"type\": 2\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        \"asrRequest\": {\n" +
                "            \"asrFormatEnum\": 0,\n" +
                "            \"asrLanguageEnum\": 0,\n" +
                "            \"asrRateEnum\": 1,\n" +
                "            \"enableITN\": true,\n" +
                "            \"enablePunctuation\": true\n" +
                "        },\n" +
                "        \"binarysState\": {\n" +
                "            \"openBinarysId\": \""+uuid+"\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        return requestJson;
    }

    //传输二进制数据
    public static JSONObject requestByte() throws IOException {
        JSONObject jsonObject = new JSONObject();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            jsonObject.put("openBinarysId",uuid);
            jsonObject.put("apiKey",apiKey);
            jsonObject.put("data",baos.toByteArray());
            return jsonObject;
        }



        //        URL resourceUrl = AsrWsDemo.class.getClassLoader().getResource(filePath);
//        if (resourceUrl == null) {
//
//            throw new FileNotFoundException("Resource not found: " + filePath);
//        }
//        String resourcePath = resourceUrl.getPath();
//        byte [] sendByte= FileUtil.read(resourcePath);
//        return sendByte;
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
