package com.link.arknights.cardpool.entity.entityForMessage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class CardMsgByPool {
    @TableId(type = IdType.AUTO)
    private int id;
    private String poolName;
    private String upName1;
    private String upName2;
    private String upUrl1;
    private String upUrl2;
    private List<Role> six;
    private String totalSix;
    private int total;
    private Long uid;
    private int startTime;
    private int stopTime;

    @Override
    public String toString() {
        return "CardMsgByPool{" +
                "id=" + id +
                ", poolName='" + poolName + '\'' +
                ", upName1='" + upName1 + '\'' +
                ", upName2='" + upName2 + '\'' +
                ", upUrl1='" + upUrl1 + '\'' +
                ", upUrl2='" + upUrl2 + '\'' +
                ", six=" + six +
                ", totalSix='" + totalSix + '\'' +
                ", total=" + total +
                ", uid=" + uid +
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

    public String getUpName1() {
        return upName1;
    }

    public void setUpName1(String upName1) {
        this.upName1 = upName1;
    }

    public String getUpName2() {
        return upName2;
    }

    public void setUpName2(String upName2) {
        this.upName2 = upName2;
    }

    public String getUpUrl1() {
        return upUrl1;
    }

    public void setUpUrl1(String upUrl1) {
        this.upUrl1 = upUrl1;
    }

    public String getUpUrl2() {
        return upUrl2;
    }

    public void setUpUrl2(String upUrl2) {
        this.upUrl2 = upUrl2;
    }

    public List<Role> getSix() {
        return six;
    }

    public void setSix(List<Role> six) {
        this.six = six;
    }

    public String getTotalSix() {
        return totalSix;
    }

    public void setTotalSix(String totalSix) {
        this.totalSix = totalSix;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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
