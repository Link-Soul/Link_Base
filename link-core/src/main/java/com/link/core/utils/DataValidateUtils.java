package com.link.core.utils;
import java.util.Collection;
import java.util.Map;

/**
 * @author Link
 * @Description  常见数据类型校验工具类。用于判断对象是否为null、空字符串、空集合等
 * @since 2025/9/11 14:52
 */
public class DataValidateUtils {

    /**
     * 判断字符串是否为空（null或空字符串）
     *
     * @param str 待校验字符串
     * @return true：为空；false：非空
     */
    public static boolean isStringEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否为非空（非null且非空字符串）
     *
     * @param str 待校验字符串
     * @return true：非空；false：为空
     */
    public static boolean isStringNotEmpty(String str) {
        return !isStringEmpty(str);
    }

    /**
     * 判断Integer是否为null或0
     * （根据业务需求可调整，此处默认0视为"空值"，如需仅判断null可修改逻辑）
     *
     * @param integer 待校验Integer
     * @return true：为null或0；false：非null且非0
     */
    public static boolean isIntegerEmpty(Integer integer) {
        return integer == null || integer == 0;
    }

    /**
     * 判断Integer是否为非null且非0
     *
     * @param integer 待校验Integer
     * @return true：非null且非0；false：为null或0
     */
    public static boolean isIntegerNotEmpty(Integer integer) {
        return !isIntegerEmpty(integer);
    }

    /**
     * 判断集合（List/Set等）是否为空（null或无元素）
     *
     * @param collection 待校验集合
     * @return true：为空；false：非空
     */
    public static boolean isCollectionEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合（List/Set等）是否为非空（非null且有元素）
     *
     * @param collection 待校验集合
     * @return true：非空；false：为空
     */
    public static boolean isCollectionNotEmpty(Collection<?> collection) {
        return !isCollectionEmpty(collection);
    }

    /**
     * 判断Map是否为空（null或无键值对）
     *
     * @param map 待校验Map
     * @return true：为空；false：非空
     */
    public static boolean isMapEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断Map是否为非空（非null且有键值对）
     *
     * @param map 待校验Map
     * @return true：非空；false：为空
     */
    public static boolean isMapNotEmpty(Map<?, ?> map) {
        return !isMapEmpty(map);
    }

    /**
     * 判断数组是否为空（null或长度为0）
     *
     * @param array 待校验数组（支持任意类型数组）
     * @return true：为空；false：非空
     */
    public static boolean isArrayEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为非空（非null且长度>0）
     *
     * @param array 待校验数组（支持任意类型数组）
     * @return true：非空；false：为空
     */
    public static boolean isArrayNotEmpty(Object[] array) {
        return !isArrayEmpty(array);
    }

    /**
     * 判断对象是否为null
     *
     * @param obj 待校验对象
     * @return true：为null；false：非null
     */
    public static boolean isObjectNull(Object obj) {
        return obj == null;
    }

    /**
     * 判断对象是否为非null
     *
     * @param obj 待校验对象
     * @return true：非null；false：为null
     */
    public static boolean isObjectNotNull(Object obj) {
        return !isObjectNull(obj);
    }

}