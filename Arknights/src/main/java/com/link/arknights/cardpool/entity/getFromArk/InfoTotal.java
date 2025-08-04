package com.link.arknights.cardpool.entity.getFromArk;

public class InfoTotal {
    private int status;
    private String msg;
    private InfoData data;

    @Override
    public String toString() {
        return "InfoTotal{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public InfoData getData() {
        return data;
    }

    public void setData(InfoData data) {
        this.data = data;
    }
}
