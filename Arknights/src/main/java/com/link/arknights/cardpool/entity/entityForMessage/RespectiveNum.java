package com.link.arknights.cardpool.entity.entityForMessage;

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

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getSix() {
        return six;
    }

    public void setSix(int six) {
        this.six = six;
    }

    public int getFive() {
        return five;
    }

    public void setFive(int five) {
        this.five = five;
    }

    public int getFour() {
        return four;
    }

    public void setFour(int four) {
        this.four = four;
    }

    public int getThree() {
        return three;
    }

    public void setThree(int three) {
        this.three = three;
    }
}
