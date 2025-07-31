package com.link.arknights.cardpool.entity.getFromArk;

public class Pool {
    private int id;
    private String poolName;
    private String up1;
    private String up2;
    private String up1Page;
    private String up2Page;
    private String state;
    private int startTime;
    private int stopTime;

    @Override
    public String toString() {
        return "Pool{" +
                "id=" + id +
                ", poolName='" + poolName + '\'' +
                ", up1='" + up1 + '\'' +
                ", up2='" + up2 + '\'' +
                ", up1Page='" + up1Page + '\'' +
                ", up2Page='" + up2Page + '\'' +
                ", state='" + state + '\'' +
                ", startTime=" + startTime +
                ", stopTime=" + stopTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getUp1() {
        return up1;
    }

    public void setUp1(String up1) {
        this.up1 = up1;
    }

    public String getUp2() {
        return up2;
    }

    public void setUp2(String up2) {
        this.up2 = up2;
    }

    public String getUp1Page() {
        return up1Page;
    }

    public void setUp1Page(String up1Page) {
        this.up1Page = up1Page;
    }

    public String getUp2Page() {
        return up2Page;
    }

    public void setUp2Page(String up2Page) {
        this.up2Page = up2Page;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }
}
