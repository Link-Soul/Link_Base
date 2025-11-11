package com.link.core.utils;

import com.link.core.common.exception.BusinessException;
import com.link.core.common.exception.code.BaseResponseCode;
import com.link.core.common.exception.code.ResponseCodeInterface;
import org.hibernate.validator.internal.engine.messageinterpolation.DefaultLocaleResolver;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;

import javax.validation.*;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * 校验工具类
 */
public class ValidatorUtils {

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws BusinessException 校验不通过，则报RRException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws BusinessException {
        ResourceBundleMessageInterpolator interpolator = new ResourceBundleMessageInterpolator(Collections.emptySet(), Locale.getDefault(), new DefaultLocaleResolver(), false);
        try (ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure()
                .messageInterpolator(interpolator).buildValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
            if (!constraintViolations.isEmpty()) {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
    }

    /**
     * 条件为true， 不报错
     *
     * @author Link
     * @since 2025/01/02 15:57
     * @param expression expression
     * @param codeInterface codeInterface
     */
    public static void isTrue(boolean expression, ResponseCodeInterface codeInterface, Object... args) {
        if (!expression) {
            throw new BusinessException(codeInterface, args);
        }
    }

    /**
     * 对象为null， 不报错
     * 不为null， 报错
     *
     * @author Link
     * @since 2024/12/30 17:31
     * @param obj 对象
     * @param objectName 对象名
     * @param objectId 对象标识
     */
    public static void isNull(Object obj, String objectName, Object objectId) {
        if (obj != null) {
            throw new BusinessException(BaseResponseCode.OBJECT_IS_NOT_NULL, String.format("%s[%s]", objectName, objectId));
        }
    }

    /**
     * 对象不为null， 不报错
     * 为null， 报错
     *
     * @author Link
     * @since 2024/12/30 17:31
     * @param obj 对象
     * @param objectName 对象名
     * @param objectId 对象标识
     */
    public static void isExists(Object obj, String objectName, Object objectId) {
        if (obj == null) {
            throw new BusinessException(BaseResponseCode.OBJECT_IS_NULL, String.format("%s[%s]", objectName, objectId));
        }
    }
}
