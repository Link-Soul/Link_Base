package com.link.core.common.exception.code;

/**
 * ResponseCodeInterface
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
public interface ResponseCodeInterface {
    /**
     * 获取code
     *
     * @return code
     */
    int getCode();

    /**
     * 获取信息
     *
     * @return msg
     */
    String getMsg();
}
