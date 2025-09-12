package com.link.core.utils;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Calendar;
/**
 * @author Link
 * @Description  * 时间日期工具类，包含LocalDateTime和Date的常用操作方法
 * @since 2025/9/12 08:20
 **/
public class LinkDateUtils {


    // 常用日期时间格式
    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_COMPACT = "yyyyMMddHHmmss";

    // 常用格式化器
    public static final DateTimeFormatter FORMATTER_DEFAULT = DateTimeFormatter.ofPattern(PATTERN_DEFAULT);
    public static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern(PATTERN_DATE);
    public static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern(PATTERN_TIME);
    public static final DateTimeFormatter FORMATTER_COMPACT = DateTimeFormatter.ofPattern(PATTERN_COMPACT);


    public static void main(String[] args) {
        String nowStr = nowStr(LinkDateUtils.FORMATTER_DEFAULT);
        System.out.println(nowStr);
    }

    // ====================== LocalDateTime 常用方法 ======================

    /**
     * 获取当前日期时间
     * @return 当前LocalDateTime
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期时间的字符串
     * @return 当前LocalDateTime
     */
    public static String nowStr() {
        return LocalDateTime.now().format(FORMATTER_DEFAULT);
    }

    /**
     * 获取当前日期时间的字符串，可选择格式化类型
     * @return 当前LocalDateTime
     */
    public static String nowStr(DateTimeFormatter formatter) {
        return LocalDateTime.now().format(formatter);
    }

    /**
     * 获取当前日期
     * @return 当前LocalDate
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * 将LocalDateTime转换为指定格式字符串
     * @param dateTime 日期时间
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将LocalDateTime转换为默认格式字符串
     * @param dateTime 日期时间
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER_DEFAULT);
    }

    /**
     * 将字符串解析为LocalDateTime
     * @param dateTimeStr 日期时间字符串
     * @param pattern 格式模式
     * @return 解析后的LocalDateTime
     */
    public static LocalDateTime parse(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将字符串解析为LocalDateTime（使用默认格式）
     * @param dateTimeStr 日期时间字符串
     * @return 解析后的LocalDateTime
     */
    public static LocalDateTime parse(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, FORMATTER_DEFAULT);
    }

    /**
     * 获取指定日期的开始时间（00:00:00）
     * @param date 日期
     * @return 当天的开始时间
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    /**
     * 获取指定日期的结束时间（23:59:59.999999999）
     * @param date 日期
     * @return 当天的结束时间
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }

    /**
     * 获取本月的第一天
     * @return 本月第一天的LocalDate
     */
    public static LocalDate firstDayOfMonth() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取本月的最后一天
     * @return 本月最后一天的LocalDate
     */
    public static LocalDate lastDayOfMonth() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 计算两个LocalDateTime之间的天数差
     * @param start 开始时间
     * @param end 结束时间
     * @return 天数差
     */
    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toDays();
    }

    /**
     * 计算两个LocalDateTime之间的小时差
     * @param start 开始时间
     * @param end 结束时间
     * @return 小时差
     */
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toHours();
    }

    /**
     * 计算两个LocalDateTime之间的分钟差
     * @param start 开始时间
     * @param end 结束时间
     * @return 分钟差
     */
    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }

    /**
     * 日期时间加减操作
     * @param dateTime 原始日期时间
     * @param days 加减天数
     * @param hours 加减小时数
     * @param minutes 加减分钟数
     * @return 计算后的日期时间
     */
    public static LocalDateTime add(LocalDateTime dateTime, long days, long hours, long minutes) {
        return dateTime.plusDays(days).plusHours(hours).plusMinutes(minutes);
    }

    // ====================== Date 常用方法 ======================

    /**
     * 将Date转换为LocalDateTime
     * @param date Date对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 将LocalDateTime转换为Date
     * @param dateTime LocalDateTime对象
     * @return Date对象
     */
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前Date
     * @return 当前Date对象
     */
    public static Date currentDate() {
        return new Date();
    }

    /**
     * 日期加减天数
     * @param date 原始日期
     * @param days 加减天数
     * @return 计算后的日期
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    /**
     * 日期加减月数
     * @param date 原始日期
     * @param months 加减月数
     * @return 计算后的日期
     */
    public static Date addMonths(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * 比较两个Date的先后
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 如果date1在date2之后返回true，否则返回false
     */
    public static boolean isAfter(Date date1, Date date2) {
        return date1.after(date2);
    }

    /**
     * 比较两个Date的先后
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 如果date1在date2之前返回true，否则返回false
     */
    public static boolean isBefore(Date date1, Date date2) {
        return date1.before(date2);
    }

    /**
     * 获取两个Date之间的天数差
     * @param start 开始日期
     * @param end 结束日期
     * @return 天数差
     */
    public static long daysBetween(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    /**
     * 判断年份是否为闰年
     * @param year 年份
     * @return 如果是闰年返回true，否则返回false
     */
    public static boolean isLeapYear(int year) {
        return Year.of(year).isLeap();
    }

    /**
     * 获取指定日期是星期几
     * @param date 日期
     * @return 星期几（1-7，1表示星期一，7表示星期日）
     */
    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.SUNDAY ? 7 : day - 1;
    }
}