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
    private Long startTime;
    private Long stopTime;

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

}
