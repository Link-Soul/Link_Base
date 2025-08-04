package com.link.arknights.cardpool.entity.getFromArk;

import lombok.Data;

@Data
public class InfoData {
    private Integer uid;
    private int guest;
    private int channelMasterId;
    private String nickName;

    @Override
    public String toString() {
        return "InfoData{" +
                "uid='" + uid + '\'' +
                ", guest=" + guest +
                ", channelMasterId=" + channelMasterId +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
