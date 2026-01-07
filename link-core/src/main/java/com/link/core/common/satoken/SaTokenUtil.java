package com.nssoftware.wakagaoagent.common.satoken;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * sa-token 补充工具类
 *
 * @author FangCheng
 * @since 2024/12/12 11:00
 **/
@Slf4j
public class SaTokenUtil {

    /**
     * 向session存入数据
     *
     * @author FangCheng
     * @since 2024/12/12 11:07
     * @param key key
     * @param value value
     */
    public static void putSession(String key, Object value) {
        if (value == null) {
            log.warn("用户{}尝试存入session的值为null，key:{}", StpUtil.getLoginIdAsString(), key);
            return;
        }
        StpUtil.getSession().set(key, value);
    }

    /**
     * 从session取数据
     *
     * @author FangCheng
     * @since 2024/12/12 11:10
     * @param key key
     * @return java.lang.Object
     */
    public static Object getSession(String key) {
        return StpUtil.getSession().get(key);
    }
}
