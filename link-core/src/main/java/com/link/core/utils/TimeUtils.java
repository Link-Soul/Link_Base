package com.link.core.utils;

import com.link.core.entity.DateMsg;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *@Description 时间 工具类
 *@author Link
 *@since 2025/11/10 14:08
 **/
public class TimeUtils {

    public static DateMsg TimeDifferenceCalculator(long timestamp1, long timestamp2) {



        // 转换为LocalDate（忽略时分秒，仅保留日期）
        LocalDate date1 = Instant.ofEpochSecond(timestamp1)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate date2 = Instant.ofEpochSecond(timestamp2)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // 确保date1是较早的日期
        if (date1.isAfter(date2)) {
            LocalDate temp = date1;
            date1 = date2;
            date2 = temp;
        }

        // 计算年、月、日差
        int years = Math.toIntExact(date1.until(date2, ChronoUnit.YEARS));
        date1 = date1.plusYears(years); // 推进年份到与date2同一年或下一年

        int months = Math.toIntExact(date1.until(date2, ChronoUnit.MONTHS));
        date1 = date1.plusMonths(months); // 推进月份到与date2同一月或下一月

        int days = Math.toIntExact(date1.until(date2, ChronoUnit.DAYS));

        // 输出结果
        return new DateMsg(years, months, days);
    }
}
