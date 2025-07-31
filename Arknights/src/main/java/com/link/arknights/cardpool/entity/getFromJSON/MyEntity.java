package com.link.arknights.cardpool.entity.getFromJSON;

import java.util.List;

public class MyEntity {
    private String p;
    private List<List<String>> c;

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public List<List<String>> getC() {
        return c;
    }

    public void setC(List<List<String>> c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "MyEntity{" +
                "p='" + p + '\'' +
                ", c=" + c +
                '}';
    }
}
