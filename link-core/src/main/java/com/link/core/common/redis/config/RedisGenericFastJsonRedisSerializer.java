package com.link.core.common.redis.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 基于 FastJson2 的 Redis 通用序列化器（安全版本）
 *
 * @author Link
 * @since 2024/12/11 14:22
 **/
public class RedisGenericFastJsonRedisSerializer implements RedisSerializer<Object> {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    // 安全的白名单配置（请根据实际项目调整）
    private static final String[] SAFE_AUTO_TYPE_WHITELIST = {
            "com.link.**",
            "java.util.**",
            "java.lang.**"
    };

    // 创建 AutoType 过滤器
    private final Filter autoTypeFilter = JSONReader.autoTypeFilter(SAFE_AUTO_TYPE_WHITELIST);

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if (o == null) {
            return new byte[0];
        }
        try {
            return JSON.toJSONBytes(o,
                    JSONWriter.Feature.WriteClassName,
                    JSONWriter.Feature.NotWriteRootClassName,
                    JSONWriter.Feature.IgnoreNoneSerializable,
                    JSONWriter.Feature.WriteEnumsUsingName
            );
        } catch (Exception e) {
            throw new SerializationException("Could not serialize: " + e.getMessage(), e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            // 使用带白名单的解析方式
            return JSON.parseObject(
                    bytes,
                    Object.class,
                    autoTypeFilter,  // 应用白名单过滤器
                    JSONReader.Feature.FieldBased,
                    JSONReader.Feature.UseDefaultConstructorAsPossible,
                    JSONReader.Feature.SupportArrayToBean
            );
        } catch (Exception e) {
            throw new SerializationException("Could not deserialize: " + e.getMessage(), e);
        }
    }
}