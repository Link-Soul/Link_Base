package com.link.arknights.cardpool.entity.entityForMessage;

public class getNumByPoolEntity {
    private String pool;
    private int sumCard;
    private int sixNum;

    @Override
    public String toString() {
        return "getNumByPoolEntity{" +
                "pool='" + pool + '\'' +
                ", sumCard=" + sumCard +
                ", sixNum=" + sixNum +
                '}';
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public int getSumCard() {
        return sumCard;
    }

    public void setSumCard(int sumCard) {
        this.sumCard = sumCard;
    }

    public int getSixNum() {
        return sixNum;
    }

    public void setSixNum(int sixNum) {
        this.sixNum = sixNum;
    }
}
