package com.link.core.common.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Link
 * @Description 配置方法获取百度翻译配置
 * @since 2025/8/18 09:59
 **/

@Component
@Data
// 该注解可以绑定配置文件中的一级地址，可以配置多个。
@ConfigurationProperties(prefix = "baidu-translation")
@Slf4j
public class BaiduTranslationProperties {

    private String url;

    private String appId;

    private String apiKey;

    private String secretKey;
}
