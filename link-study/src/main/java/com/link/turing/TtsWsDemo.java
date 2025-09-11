package com.link.turing;



import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

/**
 * ws协议请求tts协议
 */
public class TtsWsDemo {

    // NLP文本
    public static final String TEXT = "欢迎来到图灵平台。";

    // 地址
    public static final String hostUrl = "ws://ws-api.turingapi.com/api/v2";
    // 均到biz的机器人信息页面获取
    public static final String apiSecret = "11F******oC";
    public static final String apiKey = "bbd7016a48f444f6a7d9c96c66b2de6e";
    public static final String deviceId = "12332323";
    public static String ttsStreamId = null; // 类级别变量

    public static boolean wsCloseFlag = false;
    public static final String OUTPUT_FILE_PATH = "d://";
    static OutputStream outputStream=null;
    static OutputStream fileOutputStream=null;
    private static final String TEST_OUTPUT_DIR = "D:\\FFOutput\\test_voices";
    private static String currentFileName = "";
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
                    send(requestText(TEXT));
                }

                @Override
                public void onMessage(String text) {
                    //服务器相应文本内容
                    System.err.println(text);
                    JSONObject object=JSONObject.parseObject(text);

                    if (object.getInteger("code")==200){
                        JSONArray results=object.getJSONObject("nlpResponse").getJSONArray("results");
                        for (int i=0;i<results.size();i++){
                            //流式解析
                            ttsStreamId=results.getJSONObject(i).getJSONObject("values").getString("ttsStreamId");
                            try {
                                outputStream = new FileOutputStream(OUTPUT_FILE_PATH+ttsStreamId+".pcm");

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (object.getInteger("code")==260){

                        int state=object.getJSONObject("ttsResponse").getInteger("state");
                        String binarysId=object.getJSONObject("ttsResponse").getString("binarysId");
                        if (state==200 && binarysId.equals(ttsStreamId)){
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // 可以关闭连接，释放资源
                            wsCloseFlag = true;
                        }
                    }

                }
                @Override
                public void onMessage(ByteBuffer bytes) {


                    // 确保目标目录存在
                    File outputDir = new File(TEST_OUTPUT_DIR);
                    if (!outputDir.exists()) {
                        outputDir.mkdirs();
                    }

                    // 生成唯一文件名
                    String fileName = UUID.randomUUID().toString().replace("-", "")+".mp3";
                    File outputFile = new File(outputDir, fileName);

                    // 写入文件
                    try (FileOutputStream fos = new FileOutputStream(outputFile);
                         FileChannel channel = fos.getChannel()) {

                        // 如果ByteBuffer是可读模式（即flip()之后），直接写入
                        channel.write(bytes);

                        // 如果ByteBuffer是可写模式（即刚接收完数据），需要先flip
                        if (bytes.hasRemaining()) {
                            bytes.flip();
                            channel.write(bytes);
                        }

                        System.out.println("文件已保存: " + outputFile.getAbsolutePath());
                    } catch (IOException e) {
                        System.err.println("保存文件时出错: " + e.getMessage());
                        e.printStackTrace();
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
            while (!webSocketClient.getReadyState().equals(1)) {
                //System.out.println("正在连接...");
                Thread.sleep(100);
            }

            MyThread webSocketThread = new MyThread(webSocketClient, requestText(TEXT));
            webSocketThread.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public static String requestText(String input) {
        //请求参数json串
        String requestJson = "{" +
                "    \"key\":\""+apiKey+"\"," +
                "    \"timestamp\":\""+System.currentTimeMillis()+"\"," +
                "    \"data\":{" +
                "        \"deviceId\":\""+deviceId+"\"," +
                "        \"requestType\":[" +
                "            2" +
                "        ]," +
                "        \"nlpRequest\":{" +
                "            \"content\":[" +
                "                {" +
                "                    \"data\":\""+input+"\"" +
                "                }" +
                "            ]," +
                "       \"clientInfo\":{" +
                "           \"robotSkill\":{" +
                "              \"50101\":{" +
                "                    \"config\":{" +
                "                        \"tone\":\"aq\"," +
                "                        \"speed\":5," +
                "                        \"format\":\"MP3_16_24\"," +
                "                        \"volume\":5," +
                "                        \"pitch\": 5," +
                "                        \"arousal\": 5" +
                "                     }" +
                "                  }" +
                "              }" +
                "           }" +
                "        }," +
                "       \"streamTts\":true" +
                "    }" +
                "}";
        return requestJson;
    }

    // 线程来发送音频与参数
    static class MyThread extends Thread {
        WebSocketClient webSocketClient;
        String sendMessage;

        public MyThread(WebSocketClient webSocketClient, String sendMessage) {
            this.webSocketClient = webSocketClient;
            this.sendMessage = sendMessage;
        }

        public void run() {
            try {
                webSocketClient.send(sendMessage);
                // 等待服务端返回完毕后关闭
                while (!wsCloseFlag) {
//                    Thread.sleep(200);
                }
                webSocketClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // 初始化文件输出流
    private void initializeFileOutputStream() {
        try {
            // 生成唯一文件名
            currentFileName = TEST_OUTPUT_DIR + "/" + UUID.randomUUID() + ".mp3";
            fileOutputStream = new FileOutputStream(currentFileName);
            System.out.println("开始写入文件: " + currentFileName);
        } catch (IOException e) {
            System.err.println("初始化文件输出流出错: " + e.getMessage());
        }
    }

    // 关闭文件输出流（例如在会话结束时调用）
    public void closeFileOutputStream() {
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
                System.out.println("文件已成功保存: " + currentFileName);
            } catch (IOException e) {
                System.err.println("关闭文件输出流出错: " + e.getMessage());
            } finally {
                fileOutputStream = null;
                currentFileName = null;
            }
        }
    }



}
