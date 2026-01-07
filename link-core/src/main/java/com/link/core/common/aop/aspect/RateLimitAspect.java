package com.link.core.common.aop.aspect;

import com.link.core.common.aop.annotation.RateLimit;
import com.link.core.common.exception.BusinessException;
import com.link.core.common.exception.code.BaseResponseCode;
import com.link.core.utils.IPUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流切面
 *
 * @author Link
 * @since 2025/07/02 13:25
 **/
@Aspect
@Component
public class RateLimitAspect {
    // 存储：key=方法+IP，value=当前窗口内的请求次数
    private final ConcurrentMap<String, AtomicInteger> countMap = new ConcurrentHashMap<>();
    // 存储：key=方法+IP，value=窗口过期时间（毫秒）
    private final ConcurrentMap<String, Long> expireMap = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取方法
        Method method = methodSignature.getMethod();
        String methodPath = method.getDeclaringClass().getName() + "." + method.getName();
        // 获取HttpServletRequest对象
        HttpServletRequest request = getRequest(joinPoint);
        // 获取客户端IP地址
        String ipAddress = IPUtil.getClientIp(request);
        String lockKey = methodPath + ":" + ipAddress;

        //  限流参数
        // 时间窗口内最大请求数
        double limit = rateLimit.limit();
        // 时间窗口（秒转毫秒）
        long timeoutMs = rateLimit.timeout() * 1000L;
        long now = System.currentTimeMillis();

        // 初始化/重置计数器（线程安全）
        AtomicInteger count = countMap.computeIfAbsent(lockKey, k -> new AtomicInteger(0));
        Long expireTime = expireMap.computeIfAbsent(lockKey, k -> now + timeoutMs);

        // 检查窗口是否过期，过期则重置
        if (now > expireTime) {
            count.set(0);
            expireMap.put(lockKey, now + timeoutMs);
        }

        // 检查是否超限（核心：先检查，不计数）
        if (count.get() >= limit) {
            throw new BusinessException(BaseResponseCode.TOO_MANY_REQUEST);
        }
        // 标记是否需要消耗令牌
        boolean needCount = Boolean.TRUE;
        Object result = null;
        try {
            // 正常执行方法体
            result = joinPoint.proceed();
        } catch (BusinessException e) {
            needCount = Boolean.FALSE;
            throw e;
        } finally {
            if (needCount) {
                count.incrementAndGet();
            }
        }
        return result;
    }

    private HttpServletRequest getRequest(ProceedingJoinPoint joinPoint) {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
