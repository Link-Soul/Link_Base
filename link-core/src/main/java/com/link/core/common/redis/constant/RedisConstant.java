package com.link.core.common.redis.constant;

import cn.hutool.core.util.StrUtil;

/**
 * redis 通用常量
 *
 * @author Link
 * @since 2025/09/16 10:43
 **/
public class RedisConstant {
    /**
     * key的分隔符
     */
    public static final String KEY_SEPARATOR = StrUtil.COLON;
    /**
     * 缓存字典明细的key
     */
    public static final String CACHE_NAME_DICT_DETAIL = "dictDetail";
    /**
     * 邮箱验证码的key
     */
    public static final String EMAIL_CAPTCHA = "emailCaptcha";
}
