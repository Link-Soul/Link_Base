package com.link.core.common.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义数据权限注解
 * @author Link
 * @since 2025/07/25 14:15
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomDataPermission {
    /**
     * 数据权限字段 默认organ_relation
     */
    String field() default "organ_relation";
}
