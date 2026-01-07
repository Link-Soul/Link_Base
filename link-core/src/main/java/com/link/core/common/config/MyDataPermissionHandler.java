package com.link.core.common.config;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.link.core.common.aop.annotation.CustomDataPermission;
import com.link.core.common.satoken.SaTokenUtil;
import com.link.core.common.satoken.constant.SaTokenConstant;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.schema.Column;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 自定义数据权限处理
 *
 * @author Link
 * @since 2025/07/25 14:15
 **/
@Slf4j
@Component
public class MyDataPermissionHandler implements DataPermissionHandler {
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        log.info("mappedStatementId:{}", mappedStatementId);
        try {
            if (!StpUtil.isLogin()) {
                // 未登录，不控制数据权限
                return where;
            }
            Class<?> clazz = Class.forName(mappedStatementId.substring(0, mappedStatementId.lastIndexOf(".")));
            String methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(".") + 1);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (!method.getName().equals(methodName)) {
                    continue;
                }
                // 获取方法自定义注解，无此注解则不控制数据权限
                CustomDataPermission annotation = method.getAnnotation(CustomDataPermission.class);
                if (annotation == null) {
                    return where;
                }
                return buildOrganRelationExpression(where, annotation.field());
            }
            // 如果类上加了注解，则处理默认方法
            CustomDataPermission annotation = clazz.getAnnotation(CustomDataPermission.class);
            if (annotation != null) {
                String keyField = annotation.field();
                String camelCaseKeyField = StrUtil.toCamelCase(keyField);
                // 取到mapper的实体类
                Class<?> entityClass = getEntityClass(clazz);
                if (entityClass == null) {
                    return where;
                }
                Field[] fields = entityClass.getDeclaredFields();
                // 判断实体类里有没有organRelation字段
                if (Arrays.stream(fields).anyMatch(field -> field.getName().equals(camelCaseKeyField))) {
                    // 处理organRelation字段
                    SqlMethod[] values = SqlMethod.values();
                    for (SqlMethod value : values) {
                        if (SqlMethod.INSERT_ONE.equals(value) || SqlMethod.UPSERT_ONE.equals(value)) {
                            // 新增语句不处理
                            continue;
                        }
                        // 处理mp 自带的方法
                        if (methodName.equals(value.getMethod())) {
                            return buildOrganRelationExpression(where, keyField);
                        }
                    }
                }
                return where;
            }

        } catch (SaTokenException ignored) {
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return where;
    }

    /**
     * 构建organRelation字段的like表达式
     */
    public Expression buildOrganRelationExpression(Expression where, String keyField) {
        String organRelation = SaTokenUtil.getSession(SaTokenConstant.SESSION_KEY_ORGAN_RELATION).toString();
        if (StrUtil.isBlank(organRelation)) {
            // organRelation为空字符，就是超级管理员，不控制数据权限
            return where;
        }
        LikeExpression likeExpression = new LikeExpression();
        likeExpression.withLeftExpression(new Column(keyField));
        likeExpression.withRightExpression(new StringValue(organRelation + "%"));
        if (where == null) {
            return likeExpression;
        }
        return new AndExpression(where, likeExpression);
    }

    /**
     * 此方法用于获取指定类所实现接口的泛型参数类型
     */
    public Class<?> getGenericType(Class<?> clazz, Class<?> interfaceClass, int index) {
        // 运用 Spring 的工具类来解析泛型类型
        Class<?>[] genericTypes = GenericTypeResolver.resolveTypeArguments(clazz, interfaceClass);
        if (genericTypes != null && genericTypes.length > index) {
            return genericTypes[index];
        }
        return null;
    }

    /**
     * 以获取 BaseMapper 的第一个泛型参数（即实体类）为例
     */
    public Class<?> getEntityClass(Class<?> mapperClass) {
        return getGenericType(mapperClass, com.baomidou.mybatisplus.core.mapper.BaseMapper.class, 0);
    }
}
