package com.link.arknights.cardpool.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.link.arknights.cardpool.entity.getFromArk.Login;
import com.link.arknights.cardpool.entity.getFromArk.TokenByPhonePasswordReq;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpClientExample {
    public String GetHttpToken() throws Exception {
        return HttpGet("https://web-api.hypergryph.com/account/info/hg");
    }

    public String GetHttpTotal(int page, String token, int channelId) throws Exception {
        // 基本格式：https://ak.hypergryph.com/user/api/inquiry/gacha?page=1&token=***=1
//        String baseurl = "https://ak.hypergryph.com/user/api/inquiry/gacha";
        String baseurl = "https://as.hypergryph.com/user/auth/v1/token_by_phone_password";
        // 三个池子：anniver_fest 、
        // https://ak.hypergryph.com/user/api/inquiry/gacha/history?uid=86670999&category=anniver_fest&size=10
        // https://ak.hypergryph.com/user/api/inquiry/gacha/history?uid=86670999&category=anniver_fest&pos=0&gachaTs=1746405097807&size=10
        String url = baseurl + "?page=" + page + "&token=" + token + "&channelId=" + channelId;
        return HttpGet(url);
    }

    public String HttpGet(String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return result;
        } finally {
            response.close();
        }
    }

    public String HttpPost(String url, String token) throws Exception {
        // 创建 HttpClient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建 POST 请求
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头信息
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setHeader("Accept", "*/*");
        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        // 创建 JSON 请求数据
        String requestBody = "{\n" +
                "\t\"appId\": 1,\n" +
                "\t\"channelMasterId\": 1,\n" +
                "\t\"channelToken\": {\n" +
                "\t\t\"token\": \"" + token + "\"\n" +
                "\t}\n" +
                "}";
        HttpEntity entity = new StringEntity(requestBody);
        // 设置请求体
        httpPost.setEntity(entity);
        // 发送请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        StringBuilder result = new StringBuilder();
        // 读取响应
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(), "utf-8"))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                result.append(line.trim());
            }
            System.out.println(result);
        }

        // 关闭连接
        response.close();
        httpClient.close();
        return String.valueOf(result);
    }

    /**
     * @param url     访问路径
     * @param headers 访问的headers，封装在map里
     * @return 返回str的token
     * @throws Exception
     */
    public Login HttpPostGetToken(String url, Map<String, String> headers, TokenByPhonePasswordReq user) throws Exception {
        // 创建 HttpClient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建 POST 请求
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头信息
        for (Map.Entry<String, String> header : headers.entrySet()) {
            httpPost.setHeader(header.getKey(), header.getValue());
        }

        /*CookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie aliyungfTc = new BasicClientCookie("aliyungf_tc", "85c233abfe037b9c351003125ed2f17a2ffb841a11bf460d92b939b6f6dd86b6");
        aliyungfTc.setDomain("as.hypergryph.com");
        aliyungfTc.setPath("/");
        BasicClientCookie gaBtspkx9610 = new BasicClientCookie("_ga_BTSPKX9610", "GS1.1.1704847685.1.0.1704847685.0.0.0");
        gaBtspkx9610.setDomain(".hypergryph.com");
        BasicClientCookie ga74340XWSQV = new BasicClientCookie("_ga_74340XWSQV", "GS1.1.1704847685.1.0.1704847685.0.0.0");
        ga74340XWSQV.setDomain(".hypergryph.com");
        BasicClientCookie gid = new BasicClientCookie("_gid", "GA1.2.1033179850.1704847686");
        gid.setDomain(".hypergryph.com");
        BasicClientCookie gaVmlpeswl6R = new BasicClientCookie("_ga_VMLPESWL6R", "GS1.1.1704847699.1.1." + new Date().getTime() + ".57.0.0");
        gaVmlpeswl6R.setDomain(".hypergryph.com");
        BasicClientCookie gatGtagUa1045480311 = new BasicClientCookie("_gat_gtag_UA_186227413_1", "1");
        gatGtagUa1045480311.setDomain(".hypergryph.com");
        BasicClientCookie gatGtagUa1045480313 = new BasicClientCookie("_gat_gtag_UA_186227413_3", "1");
        gatGtagUa1045480313.setDomain(".hypergryph.com");
        BasicClientCookie ga = new BasicClientCookie("_ga", "GA1.2.128435991.1704847685");
        ga.setDomain(".hypergryph.com");
        cookieStore.addCookie(aliyungfTc);
        cookieStore.addCookie(gaBtspkx9610);
        cookieStore.addCookie(ga74340XWSQV);
        cookieStore.addCookie(gid);
        cookieStore.addCookie(gaVmlpeswl6R);
        cookieStore.addCookie(gatGtagUa1045480311);
        cookieStore.addCookie(gatGtagUa1045480313);
        cookieStore.addCookie(ga);

        BasicHttpContext basicHttpContext = new BasicHttpContext();
        basicHttpContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);*/

/*        TokenByPhonePasswordReq requestObject = new TokenByPhonePasswordReq();
        requestObject.setPhone("13287845587");
        requestObject.setPassword("40580087");*/

        // 将对象转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(user);

        // 将 JSON 字符串放入请求体
        StringEntity stringEntity = new StringEntity(jsonBody);
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Content-Type", "application/json");

        // 发送请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        StringBuilder result = new StringBuilder();

        // 读取响应
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                result.append(line.trim());
            }
        }

        // 关闭连接
        response.close();
        httpClient.close();
        return JSON.parseObject(String.valueOf(result), Login.class);
    }

    public Login LogOut(String url, Map<String, String> headers, String token) throws Exception {
        // 创建 HttpClient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建 POST 请求
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头信息
        for (Map.Entry<String, String> header : headers.entrySet()) {
            httpPost.setHeader(header.getKey(), header.getValue());
        }


        // 创建 JSON 请求数据 这里请求体是账号密码
        String requestBody = "{\n" +
                "  \"token\": \"" + token + "\"\n" +
                "}";
        HttpEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        // 设置请求体
        httpPost.setEntity(entity);
        // 发送请求


        CloseableHttpResponse response = httpClient.execute(httpPost);
        StringBuilder result = new StringBuilder();
        // 读取响应
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                result.append(line.trim());
            }
            System.out.println("退出登录");
            System.out.println(result);
        }


        // 关闭连接
        response.close();
        httpClient.close();
        return JSON.parseObject(String.valueOf(result), Login.class);
    }

}