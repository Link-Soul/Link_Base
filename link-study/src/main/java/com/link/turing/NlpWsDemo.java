package com.link.turing;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 *
 */
public class NlpWsDemo {

    // NLP文本
    public static final String TEXT = "你叫什么名字";

    // 地址
    public static final String hostUrl = "ws://ws-api.turingapi.com/api/v2";
    // 均到biz的机器人信息页面获取
    public static final String apiSecret = "11F******oC";
    public static final String apiKey = "bbd7016a48f444f6a7d9c96c66b2de6e";
    public static final String deviceId = "12332323";


    public static boolean wsCloseFlag = false;

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
                    //服务器相应内容
                    System.out.println(text);

                    // 可以关闭连接，释放资源
                    wsCloseFlag = true;
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
            while (!webSocketClient.getReadyState().equals(org.java_websocket.enums.ReadyState.OPEN)) {
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
                "    \"key\":\"" + apiKey + "\"," +
                "    \"timestamp\": \""+System.currentTimeMillis()+"\"," +
                "    \"data\": {" +
                "        \"deviceId\": \""+deviceId+"\"," +
                "        \"requestType\": [" +
                "            1" +
                "        ]," +
                "        \"nlpRequest\": {" +
                "            \"content\": [" +
                "                {" +
                "                    \"data\": \"" + input + "\"" +
                "                }" +
                "            ]" +
                "        }" +
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
                    Thread.sleep(200);
                }
                webSocketClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
