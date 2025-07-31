package com.link.arknights.cardpool.entity.getFromArk;

import java.util.List;

public class DataList {
    /**
     * 时间戳
     */
    private Long ts;
    /**
     * 卡池名称
     */
    private String pool;
    /**
     * 该次抽卡信息
     */
    private List<Single> chars;

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public List<Single> getChars() {
        return chars;
    }

    public void setChars(List<Single> chars) {
        this.chars = chars;
    }

    @Override
    public String toString() {
        return "DataList{" +
                "ts=" + ts +
                ", pool='" + pool + '\'' +
                ", chars=" + chars +
                '}';
    }
}
