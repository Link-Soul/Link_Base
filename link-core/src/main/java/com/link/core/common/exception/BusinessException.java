package com.link.core.common.exception;

import com.link.core.common.exception.code.ResponseCodeInterface;

/**
 * BusinessException
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
public class BusinessException extends RuntimeException {
    /**
     * 异常编号
     */
    private final int messageCode;

    /**
     * 对messageCode 异常信息进行补充说明
     */
    private final String detailMessage;

    private final Object[] args;
    /**
     * 构造函数
     *
     * @param code 异常码
     */
    public BusinessException(ResponseCodeInterface code) {
        super(code.getMsg());
        this.messageCode = code.getCode();
        this.detailMessage = code.getMsg();
        this.args = null;
    }

    /**
     * 构造函数
     *
     * @param code 异常码
     * @param args 消息参数
     */
    public BusinessException(ResponseCodeInterface code, Object... args) {
        super(code.getMsg());
        this.messageCode = code.getCode();
        this.detailMessage = code.getMsg();
        this.args = args;
    }

    /**
     * 构造函数
     *
     * @param code 异常码
     * @param detailMessage detailMessage
     */
    public BusinessException(int code, String detailMessage) {
        super(detailMessage);
        this.messageCode = code;
        this.detailMessage = detailMessage;
        this.args = null;
    }

    public int getMessageCode() {
        return messageCode;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public Object[] getArgs() {
        return args;
    }
}
