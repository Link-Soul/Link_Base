package com.nssoftware.wakagaoagent.common.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {
    //限制次数
    double limit() default 1.0;
    //限制时间 秒
    int timeout() default 60;
}
