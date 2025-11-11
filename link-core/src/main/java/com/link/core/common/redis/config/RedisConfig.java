package com.link.core.common.redis.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * redis配置类
 *
 * @author Link
 * @since 2024/12/11 14:17
 **/
@Configuration
@EnableCaching
@EnableConfigurationProperties({RedisProperties.class})
@Slf4j
public class RedisConfig {
    @Autowired
    private CacheManagerProperties cacheManagerProperties;

    @Bean
    public RedisSerializer<String> keySerializer(){
        return new StringRedisSerializer(StandardCharsets.UTF_8);
    }

    @Bean
    public RedisSerializer<Object> valueSerializer(){
        return new RedisGenericFastJsonRedisSerializer();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, RedisSerializer<String> keySerializer, RedisSerializer<Object> valueSerializer) {
        RedisTemplate<String,Object> template = new RedisTemplate<>();

        template.setDefaultSerializer(keySerializer);
        template.setKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(valueSerializer);

        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    @Primary
    @Bean(name = "cacheManager")
    public RedisCacheManager cacheManager(RedisConnectionFactory factory, RedisSerializer<String> keySerializer, RedisSerializer<Object> valueSerializer) {
        if (log.isDebugEnabled()) {
            log.debug("cacheManagerProperties： {}", this.cacheManagerProperties);
        }

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                .entryTtl(Duration.ofSeconds(cacheManagerProperties.getKeyExpire()))
                .computePrefixWith(name -> name + cacheManagerProperties.getKeySeparator());

        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("redis-plugin:");

        return  RedisCacheManager
                .builder(factory)
                .cacheDefaults(redisCacheConfiguration)//这一句必须要最先执行，否则实际运行时使用的是defaultConfig
                .initialCacheNames(cacheNames)
                .build();
    }

    @Bean(name = "longCacheManager")
    public RedisCacheManager longCacheManager(RedisConnectionFactory factory, RedisSerializer<String> keySerializer, RedisSerializer<Object> valueSerializer) {
        if (log.isDebugEnabled()) {
            log.debug("cacheManagerProperties： {}", this.cacheManagerProperties);
        }

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                .entryTtl(Duration.ofSeconds(cacheManagerProperties.getKeyLongExpire()))
                .computePrefixWith(name -> name + cacheManagerProperties.getKeySeparator());

        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("redis-plugin:");

        return  RedisCacheManager
                .builder(factory)
                .cacheDefaults(redisCacheConfiguration)//这一句必须要最先执行，否则实际运行时使用的是defaultConfig
                .initialCacheNames(cacheNames)
                .build();
    }

    /**
     * 加密key
     *
     * @author Link
     * @since 2024/12/12 13:58
     * @return org.springframework.cache.interceptor.KeyGenerator
     */
    @Bean
    public KeyGenerator keyGenSecure() {
        return keyGenerator(true, false);
    }

    /**
     * 明文Key
     *
     * @author Link
     * @since 2024/12/12 13:58
     * @return org.springframework.cache.interceptor.KeyGenerator
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return keyGenerator(false, false);
    }

    /**
     * 加密key
     *
     * @author Link
     * @since 2024/12/12 13:58
     * @return org.springframework.cache.interceptor.KeyGenerator
     */
    @Bean
    public KeyGenerator keyGenSecureWithCompany() {
        return keyGenerator(true, true);
    }


    /**
     * 明文Key
     *
     * @author Link
     * @since 2024/12/12 13:58
     * @return org.springframework.cache.interceptor.KeyGenerator
     */
    @Bean
    public KeyGenerator keyGeneratorWithCompany() {
        return keyGenerator(false, true);
    }

    private KeyGenerator keyGenerator(boolean encrypt, boolean withCompany) {
        return (target, method, params) -> {
            String className = target.getClass().getSimpleName();
            String methodName = method.getName();

            String paramName = Arrays.stream(params).map(res -> encrypt ? SecureUtil.md5(res == null ? StrUtil.EMPTY : String.valueOf(res)) : String.valueOf(res))
                    .collect(Collectors.joining(cacheManagerProperties.getKeySeparator()));

            String result = className
                    .concat(cacheManagerProperties.getKeySeparator())
                    .concat(methodName)
                    .concat(cacheManagerProperties.getKeySeparator())
                    .concat(paramName);
            if (withCompany) {
                // 将企业id放到前面
                String companyId = StrUtil.EMPTY;
//                try {
//                    companyId = SaTokenUtil.getSession(SaTokenConstant.SESSION_KEY_ORGAN_RELATION).toString();
//                } catch (SaTokenException ignored) {
//                }

                result = companyId
                        .concat(cacheManagerProperties.getKeySeparator())
                        .concat(result);
            }
            return result;
        };
    }
}
