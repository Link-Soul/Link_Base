package com.link.arknights.cardpool.entity.getFromArk;

public class InfoData {
    private Long uid;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int getGuest() {
        return guest;
    }

    public void setGuest(int guest) {
        this.guest = guest;
    }

    public int getChannelMasterId() {
        return channelMasterId;
    }

    public void setChannelMasterId(int channelMasterId) {
        this.channelMasterId = channelMasterId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
