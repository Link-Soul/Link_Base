package com.link.arknights.cardpool.entity.getFromArk;

public class Pagination {
    private int current;
    private int total;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "current=" + current +
                ", total=" + total +
                '}';
    }
}
