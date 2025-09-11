package com.link.core.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.link.core.entity.ToolString;
import org.apache.commons.codec.binary.Base64;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * @author Link
 * @Description Http 请求通用方法（基于 HttpClient 5）
 * @since 2025/9/8 16:00
 **/
public class HttpUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    // 超时时间配置（Java 8 下使用 Timeout 类）
    private static final Timeout CONNECT_TIMEOUT = Timeout.ofMilliseconds(10_000);    // 连接建立超时（10秒）
    private static final Timeout REQUEST_TIMEOUT = Timeout.ofMilliseconds(15_000);   // 从连接池获取连接的超时（15秒）
    private static final Timeout RESPONSE_TIMEOUT = Timeout.ofMilliseconds(20_000);  // 响应超时（等待服务端返回数据的时间，20秒）

    // 连接池配置（根据业务场景调整）
    private static final int MAX_TOTAL_CONN = 200;       // 总连接池最大连接数
    private static final int MAX_CONN_PER_ROUTE = 50;    // 每个路由（如同一域名）的最大连接数

    // 重试策略配置（可选）
    private static final int RETRY_COUNT = 5;           // 最大重试次数（默认 3 次）
    private static final boolean REQUEST_SENT_RETRY = true; // 是否在请求已发送后重试（默认 false）

    /**
     * 创建自定义的 HttpClient 实例
     *
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient createCustomHttpClient() {
        // 1. 创建连接池管理器
        PoolingHttpClientConnectionManager connectionManager = createConnectionManager();

        // 2. 配置请求参数
        RequestConfig requestConfig = createRequestConfig();

        // 3. 创建重试策略
        HttpRequestRetryStrategy retryStrategy = createRetryStrategy();

        // 4. 构建 HttpClient
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryStrategy(retryStrategy)
                .build();
    }

    /**
     * 创建连接池管理器
     *
     * @return PoolingHttpClientConnectionManager
     */
    private static PoolingHttpClientConnectionManager createConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager();

        // 设置最大连接数
        connectionManager.setMaxTotal(MAX_TOTAL_CONN);

        // 设置每个路由的最大连接数
        connectionManager.setDefaultMaxPerRoute(MAX_CONN_PER_ROUTE);

        // 配置Socket选项（可选）
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(RESPONSE_TIMEOUT) // 响应超时
                .build();
        connectionManager.setDefaultSocketConfig(socketConfig);

        return connectionManager;
    }

    /**
     * 创建请求参数配置
     *
     * @return RequestConfig
     */
    private static RequestConfig createRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)      // 连接建立超时
                .setConnectionRequestTimeout(REQUEST_TIMEOUT) // 从连接池获取连接的超时
                .setResponseTimeout(RESPONSE_TIMEOUT)    // 响应超时
                .build();
    }

    /**
     * 创建重试策略
     *
     * @return HttpRequestRetryStrategy
     */
    private static HttpRequestRetryStrategy createRetryStrategy() {
        return new HttpRequestRetryStrategy() {
            // 处理因IOException导致的重试
            @Override
            public boolean retryRequest(HttpRequest request,
                                        IOException exception,
                                        int execCount,
                                        HttpContext context) {
                // 达到最大重试次数，不再重试
                if (execCount > RETRY_COUNT) {
                    return false;
                }

                // 请求已发送且配置不允许重试，不再重试
                Boolean requestSent = (Boolean) context.getAttribute("http.request_sent");
                if (requestSent != null && requestSent && !REQUEST_SENT_RETRY) {
                    return false;
                }

                // 特定异常不重试
                if (exception instanceof InterruptedIOException || // 超时或中断
                        exception instanceof UnknownHostException ||   // 未知主机
                        exception instanceof SSLException) {           // SSL错误
                    return false;
                }
                // 其他IO异常可以重试
                return true;
            }

            // 处理因HTTP响应导致的重试（如5xx状态码）
            @Override
            public boolean retryRequest(HttpResponse response,
                                        int execCount,
                                        HttpContext context) {
                // 达到最大重试次数，不再重试
                if (execCount > RETRY_COUNT) {
                    return false;
                }

                // 只在服务器错误时重试（5xx状态码）
                int statusCode = response.getCode();
                return statusCode >= 500 && statusCode < 600;
            }

            @Override
            public TimeValue getRetryInterval(HttpResponse response, int execCount, HttpContext context) {
                // 重试间隔，随着重试次数增加而增加（指数退避）
                return TimeValue.ofSeconds((long) Math.pow(2, execCount));
                // 固定时间间隔示例
                // return TimeValue.ofSeconds(1);

                // 根据Retry-After头信息示例：
                /*
                Header retryAfterHeader = response.getFirstHeader("Retry-After");
                if (retryAfterHeader != null) {
                    try {
                        long seconds = Long.parseLong(retryAfterHeader.getValue());
                        return TimeValue.ofSeconds(seconds);
                    } catch (NumberFormatException e) {
                        // 处理日期格式的Retry-After头（略复杂）
                    }
                }
                return TimeValue.ofSeconds(1);
                */
            }
            // 注意：在HttpClient5.0中，还有一个getRetryInterval方法用于异常情况，但接口中只有一个getRetryInterval方法，它用于响应和异常两种情况？
            // 实际上，接口中只有一个getRetryInterval方法，它用于两种情况。所以我们需要根据情况返回间隔。
            // 但是，上面的重试间隔我们根据响应情况返回，异常情况也会调用同一个方法吗？
            // 实际上，HttpClient5.0中，HttpRequestRetryStrategy接口有三个方法？我可能记错了。

            // 经过查阅HttpClient5.1的源码，HttpRequestRetryStrategy接口确实只有两个retryRequest方法和一个getRetryInterval方法。
            // 但是，getRetryInterval方法在两个重试情况下都会被调用，所以我们不能区分是异常还是响应。这里我们统一返回一个间隔。
            // 不过，我们可以在getRetryInterval方法中根据上下文来区分，但这样比较复杂。

            // 为了简单，我们返回固定间隔1秒，或者使用指数退避（但注意指数退避可能会在响应重试和异常重试中都使用）

        };
    }

    /**
     * Url Base64编码
     */
    public static String encode(String data) {
        String str = null;
        try {
            byte[] b = Base64.encodeBase64URLSafe(data.getBytes(ToolString.encoding));
            str = new String(b, ToolString.encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Url Base64解码
     */
    public static String decode(String data) {
        String str = null;
        try {
            byte[] b = Base64.decodeBase64(data.getBytes(ToolString.encoding));
            str = new String(b, ToolString.encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * URL编码（utf-8）
     */
    public static String urlEncode(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, ToolString.encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * URL解码（utf-8）
     */
    public static String urlDecode(String source) {
        String result = source;
        try {
            result = java.net.URLDecoder.decode(source, ToolString.encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 执行 application/x-www-form-urlencoded 类型的 POST 请求
     */
    public static String executeFormRequest(String url, List<NameValuePair> params) throws IOException, URISyntaxException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);

            // 设置请求体（HttpClient 5 中 UrlEncodedFormEntity 构造直接传字符集）
            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 执行带自定义请求头的 application/x-www-form-urlencoded POST 请求
     */
    public static String executeFormRequestWithHeaders(String url, List<NameValuePair> params, NameValuePair... headers)
            throws IOException, URISyntaxException {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

            // 添加自定义请求头（HttpClient 5 中 Header 接口保持兼容）
            for (NameValuePair header : headers) {
                httpPost.addHeader(header.getName(), header.getValue());
            }

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 执行 GET 请求
     */
    public static String doGet(String url) throws IOException {
        return doGet(url, null, null);
    }

    /**
     * 执行带请求头的 GET 请求
     */
    public static String doGet(String url, Map<String, String> headers) throws IOException {
        return doGet(url, headers, null);
    }

    /**
     * 执行带请求头和查询参数的 GET 请求
     * （HttpClient 5 推荐使用 URIBuilder 构建带参数的URL）
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        try (CloseableHttpClient httpClient = createCustomHttpClient()) {
            // 构建带参数的URL（使用 HttpClient 5 的 URIBuilder）
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());

            setHeaders(httpGet, headers);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                return handleResponse(response);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行 POST 请求（application/x-www-form-urlencoded）
     */
    public static String doPostForm(String url, Object entity) throws IOException {
        return doPostForm(url, entity, null);
    }

    /**
     * 执行带请求头的 POST 请求（application/x-www-form-urlencoded）
     */
    public static String doPostForm(String url, Object entity, Map<String, String> headers) throws IOException {
        try (CloseableHttpClient httpClient = createCustomHttpClient()) {
            HttpPost httpPost = new HttpPost(url);

            setHeaders(httpPost, headers);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

            List<NameValuePair> params = formatEntityToPairs(entity);
            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return handleResponse(response);
            }
        }
    }

    /**
     * 执行 POST 请求（application/json）
     */
    public static String doPostJson(String url, String jsonBody) throws IOException {
        return doPostJson(url, jsonBody, null);
    }

    /**
     * 执行带请求头的 POST 请求（application/json）
     */
    public static String doPostJson(String url, String jsonBody, Map<String, String> headers) throws IOException {
        try (CloseableHttpClient httpClient = createCustomHttpClient()) {
            HttpPost httpPost = new HttpPost(url);

            setHeaders(httpPost, headers);
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");

            if (jsonBody != null) {
                httpPost.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));
            }

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return handleResponse(response);
            }
        }
    }

    /**
     * 执行 POST 请求（application/json），自动将对象转换为JSON
     */
    public static String doPostJson(String url, Object entity) throws IOException {
        return doPostJson(url, entity, null);
    }

    /**
     * 执行带请求头的 POST 请求（application/json），自动将对象转换为JSON
     */
    public static String doPostJson(String url, Object entity, Map<String, String> headers) throws IOException {
        String jsonBody = OBJECT_MAPPER.writeValueAsString(entity);
        return doPostJson(url, jsonBody, headers);
    }

    /**
     * 执行 PUT 请求（application/json）
     */
    public static String doPutJson(String url, String jsonBody) throws IOException {
        return doPutJson(url, jsonBody, null);
    }

    /**
     * 执行带请求头的 PUT 请求（application/json）
     */
    public static String doPutJson(String url, String jsonBody, Map<String, String> headers) throws IOException {
        try (CloseableHttpClient httpClient = createCustomHttpClient()) {
            HttpPut httpPut = new HttpPut(url);

            setHeaders(httpPut, headers);
            httpPut.setHeader("Content-Type", "application/json; charset=utf-8");

            if (jsonBody != null) {
                httpPut.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));
            }

            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                return handleResponse(response);
            }
        }
    }

    /**
     * 执行 PUT 请求（application/json），自动将对象转换为JSON
     */
    public static String doPutJson(String url, Object entity) throws IOException {
        return doPutJson(url, entity, null);
    }

    /**
     * 执行带请求头的 PUT 请求（application/json），自动将对象转换为JSON
     */
    public static String doPutJson(String url, Object entity, Map<String, String> headers) throws IOException {
        String jsonBody = OBJECT_MAPPER.writeValueAsString(entity);
        return doPutJson(url, jsonBody, headers);
    }

    /**
     * 执行 DELETE 请求
     */
    public static String doDelete(String url) throws IOException {
        return doDelete(url, null);
    }

    /**
     * 执行带请求头的 DELETE 请求
     */
    public static String doDelete(String url, Map<String, String> headers) throws IOException {
        try (CloseableHttpClient httpClient = createCustomHttpClient()) {
            HttpDelete httpDelete = new HttpDelete(url);

            setHeaders(httpDelete, headers);

            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
                return handleResponse(response);
            }
        }
    }


    /**
     * 执行带请求体的 DELETE 请求（application/json），自动将对象转换为JSON
     */
    public static String doDeleteWithBody(String url, Object entity, Map<String, String> headers) throws IOException {
        String jsonBody = OBJECT_MAPPER.writeValueAsString(entity);
        return doDeleteWithBody(url, jsonBody, headers);
    }

    /**
     * 执行带请求头的 PATCH 请求（application/json）
     */
    public static String doPatchJson(String url, String jsonBody, Map<String, String> headers) throws IOException {
        try (CloseableHttpClient httpClient = createCustomHttpClient()) {
            HttpPatch httpPatch = new HttpPatch(url);

            setHeaders(httpPatch, headers);
            httpPatch.setHeader("Content-Type", "application/json; charset=utf-8");

            if (jsonBody != null) {
                httpPatch.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));
            }

            try (CloseableHttpResponse response = httpClient.execute(httpPatch)) {
                return handleResponse(response);
            }
        }
    }

    /**
     * 执行 PATCH 请求（application/json），自动将对象转换为JSON
     */
    public static String doPatchJson(String url, Object entity) throws IOException {
        return doPatchJson(url, entity, null);
    }

    /**
     * 执行带请求头的 PATCH 请求（application/json），自动将对象转换为JSON
     */
    public static String doPatchJson(String url, Object entity, Map<String, String> headers) throws IOException {
        String jsonBody = OBJECT_MAPPER.writeValueAsString(entity);
        return doPatchJson(url, jsonBody, headers);
    }


    /**
     * 设置请求头（兼容 HttpClient 5 的 HttpUriRequest 接口）
     */
    private static void setHeaders(HttpUriRequest request, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 处理HTTP响应（HttpClient 5 中状态码获取方式不变）
     */
    private static String handleResponse(CloseableHttpResponse response) throws IOException {
        int statusCode = response.getCode(); // HttpClient 5 中 getStatusLine().getStatusCode() 简化为 getCode()
        HttpEntity entity = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        EntityUtils.consume(entity);

        if (statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES) {
            return result;
        } else {
            throw new IOException("HTTP请求失败，状态码: " + statusCode + "，响应: " + result);
        }
    }

    /**
     * 将实体转换为 NameValuePair 列表（逻辑不变，适配 HttpClient 5 的 BasicNameValuePair）
     */
    public static List<NameValuePair> formatEntityToPairs(Object entity) {
        List<NameValuePair> pairs = new ArrayList<>();
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(entity);
                if (fieldValue == null) {
                    continue;
                }

                String key = getFieldKey(field);

                if (fieldValue instanceof Collection || fieldValue.getClass().isArray()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(fieldValue);
                    pairs.add(new BasicNameValuePair(key, json));
                } else {
                    pairs.add(new BasicNameValuePair(key, fieldValue.toString()));
                }
            } catch (IllegalAccessException | JsonProcessingException e) {
                throw new RuntimeException("格式化字段失败: " + field.getName(), e);
            }
        }
        return pairs;
    }

    /**
     * 获取字段对应的键名（逻辑不变）
     */
    private static String getFieldKey(Field field) {
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        if (jsonProperty != null && !jsonProperty.value().isEmpty()) {
            return jsonProperty.value();
        }
        return field.getName();
    }
}