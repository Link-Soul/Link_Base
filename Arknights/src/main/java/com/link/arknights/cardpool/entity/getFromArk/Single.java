package com.link.arknights.cardpool.entity.getFromArk;

public class Single {
    /**
     * 干员名称
     */
    private String name;
    /**
     * 星级-1 （六星对应5）
     */
    private int rarity;
    /**
     * 是否为new
     */
    private String isNew;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    @Override
    public String toString() {
        return "Single{" +
                "name='" + name + '\'' +
                ", rarity=" + rarity +
                ", isNew=" + isNew +
                '}';
    }
}
