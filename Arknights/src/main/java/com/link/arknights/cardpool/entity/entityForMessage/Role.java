package com.link.arknights.cardpool.entity.entityForMessage;

public class Role {
    private String name;
    private String url;
    private int num;
    private Long time;

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", num=" + num +
                ", time=" + time +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
