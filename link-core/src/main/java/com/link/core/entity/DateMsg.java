package com.link.core.entity;

import lombok.Data;

/**
 *@Description 时间 实体类
 *@author Link
 *@since 2025/11/10 14:09
 **/
@Data
public class DateMsg {
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;
    private Integer second;

    public DateMsg(int years, int months, int days) {
        this.year = years;
        this.month = months;
        this.day = days;
    }
}
