package com.link.core.common.exception.handler;


import com.link.core.common.exception.BusinessException;
import com.link.core.common.exception.code.BaseResponseCode;
import com.link.core.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * RestExceptionHandler
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    /**
     * 自定义全局异常处理
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    DataResult businessExceptionHandler(BusinessException e) {
        log.error("BusinessException:", e);
        return DataResult.getResult(e.getMessageCode(), e.getDetailMessage());
    }



    /**
     * 处理validation 框架异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    DataResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidExceptionHandler bindingResult.allErrors():{},exception:", e.getBindingResult().getAllErrors(), e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError error : fieldErrors) {
            sb.append(error.getField()).append(error.getDefaultMessage()).append("; ");
        }
        return DataResult.getResult(BaseResponseCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getCode(), sb.toString());
    }

    /**
     * 校验List<entity>类型， 需要controller添加@Validated注解
     * 处理Validated List<entity> 异常
     */
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DataResult handle(ConstraintViolationException exception) {
        log.error("methodConstraintViolationExceptionHandler bindingResult.allErrors():{},exception:", exception, exception);
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            builder.append(violation.getPropertyPath()).append(violation.getMessage()).append("; ");
        }
        return DataResult.getResult(BaseResponseCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getCode(), builder.toString());
    }

    /**
     * 校验List<entity>类型， 需要controller添加@Validated注解
     * 处理Validated List<entity> 异常
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DataResult handle(BindException exception) {
        log.error("methodBindExceptionHandler bindingResult.allErrors():{},exception:", exception.getBindingResult().getAllErrors(), exception);
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError error : fieldErrors) {
            sb.append(error.getField()).append(error.getDefaultMessage()).append("; ");
        }
        return DataResult.getResult(BaseResponseCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getCode(), sb.toString());
    }

    /**
     * 校验List<entity>类型， 需要controller添加@Validated注解
     * 处理Validated List<entity> 异常
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DataResult handle(MissingServletRequestParameterException exception) {
        log.error("methodMissingServletRequestParameterExceptionHandler exception", exception);
        String errorMessage = String.format("Required parameter '%s' is missing", exception.getParameterName());
        return DataResult.getResult(BaseResponseCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getCode(), errorMessage);
    }


    /**
     * 未登录异常
     */
//    @ExceptionHandler(NotLoginException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public DataResult handle(NotLoginException exception) {
//        log.error("NotLoginException: ", exception);
//        BaseResponseCode responseCode = BaseResponseCode.INVALID_TOKEN;
//        if (exception.getType().equals(NotLoginException.NOT_TOKEN)) {
//            responseCode = BaseResponseCode.NOT_TOKEN;
//        }
//        return DataResult.getResult(responseCode.getCode(), responseCode.getMsg());
//    }

    /**
     * 无权限异常
     */
//    @ExceptionHandler(NotPermissionException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public DataResult handle(NotPermissionException exception) {
//        log.error("NotPermissionException: ", exception);
//        return DataResult.getResult(BaseResponseCode.UNAUTHORIZED_ERROR.getCode(), BaseResponseCode.UNAUTHORIZED_ERROR.getMsg());
//    }

    /**
     * 系统繁忙，请稍候再试
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DataResult handleException(Throwable e) {
        log.error("Exception,exception:", e);
        return DataResult.getResult(BaseResponseCode.SYSTEM_BUSY);
    }

}
