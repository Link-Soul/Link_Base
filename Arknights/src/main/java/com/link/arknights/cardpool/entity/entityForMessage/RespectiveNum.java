package com.link.arknights.cardpool.entity.entityForMessage;

import lombok.Data;

@Data
public class RespectiveNum {
    private String pool;
    private int sum;
    private int six;
    private int five;
    private int four;
    private int three;

    @Override
    public String toString() {
        return "RespectiveNum{" +
                "name='" + pool + '\'' +
                ", sum=" + sum +
                ", six=" + six +
                ", five=" + five +
                ", four=" + four +
                ", three=" + three +
                '}';
    }

}
