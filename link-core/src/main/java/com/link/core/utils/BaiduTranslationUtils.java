package com.link.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.link.core.common.properties.BaiduTranslationProperties;
import com.link.core.enums.LanguageEnum;
import com.link.core.vo.resp.BaiduTranslateRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 *@Description 百度翻译对接的翻译工具类
 *@author Link
 *@since 2026/2/2 08:44
 **/
@Slf4j
@Component
public class BaiduTranslationUtils {

    @Resource
    private BaiduTranslationProperties properties;

    private static String url;
    private static String appId;
    private static String apiKey;
    private static String secretKey;

    // 初始化时从配置中读取并赋值给静态变量
    @PostConstruct
    public void init() {
        // 获取当前激活的邮箱配置
        String urlProperties = properties.getUrl();
        String appIdProperties = properties.getAppId();
        String apiKeyProperties = properties.getApiKey();
        String secretKeyProperties = properties.getSecretKey();

        if (StrUtil.isBlank(urlProperties) ||StrUtil.isBlank(appIdProperties)
                ||StrUtil.isBlank(apiKeyProperties) ||StrUtil.isBlank(secretKeyProperties)) {
            log.error("百度翻译配置未初始化请检查配置。url:{},appId:{},apiKey:{},secretKey:{}", url, appId, apiKey, secretKey);
            return;
        }
        // 赋值给静态变量
        url = urlProperties;
        appId = appIdProperties;
        apiKey = apiKeyProperties;
        secretKey = secretKeyProperties;
    }


    /**
     * 基础翻译
     *
     * @author Link
     * @since 2026/2/2 11:10
     * @param text      要翻译的文本
     * @param language  要翻译的目标语言
     * @return
     */
    public static String translation(String text, LanguageEnum language) {
        String accessToken = getAccessToken();
        if (StrUtil.isBlank(text) || language == null) {
            throw new RuntimeException(String.format("参数异常；翻译内容：%s；选择语言：%s", text, language==null?"未选择语言":language.getDesc()));
        }
        String formatUrl = url+"?access_token="+accessToken;
        JSONObject reqJson = new JSONObject();
        reqJson.put("from", "auto");
        reqJson.put("to", language.getCode());
        reqJson.put("q", text);
        reqJson.put("termIds", "");
        BaiduTranslateRespVO respVO = null;
        String resp = "";
        try {
            resp = HttpUtil.post(formatUrl, reqJson.toString());
            respVO = JSONObject.parseObject(resp, BaiduTranslateRespVO.class);
        } catch (Exception e) {
            log.error("百度翻译出现异常；请求体：{};返回值：{}",reqJson, resp, e);
            return null;
        }
        if (respVO == null) {
            log.error("百度翻译报错：{}", resp);
            return null;
        }
        return respVO.getResult().getTransResult().get(0).getDst();
    }


    /**
     * 获取token
     *      TODO 后续可以加上缓存，因为生效时间30天，或者定时任务来搞
     *
     * @author Link
     * @since 2026/2/2 09:16
     * @return 从百度平台获取的token
     */
    private static String getAccessToken() {

        String token = reqGetToken();

        return token;
    }

    public static String reqGetToken() {
        // 若缓存未击中则请求
        String formatUrl = String.format("https://aip.baidubce.com/oauth/2.0/token" +
                "?grant_type=client_credentials&client_id=%s&client_secret=%s", apiKey, secretKey);

        try (HttpResponse response = HttpRequest.post(formatUrl)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .timeout(20000)//超时，毫秒
                .execute()){
            // 判断状态码
            if (response.getStatus() != 200) {
                log.error("第三方接口返回非成功状态码:{}，响应体：{}",response.getStatus(),response.body());
            }
            String body = response.body();
            JSONObject respJson = JSONObject.parseObject(body);
            if (respJson.containsKey("refresh_token")){
                return respJson.getString("access_token");
            } else {
                log.error("从百度平台获取token失败，未找到token信息。报文：{}",body);
                return null;
            }
        } catch (Exception e){
            log.error("向百度平台获取token失败。", e);
        }
        return null;
    }

}
