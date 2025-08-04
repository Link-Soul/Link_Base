package com.link.arknights.cardpool.entity.getFromArk;

/**
 * 抽卡信息
 */
public class Total {
    /**
     * 大概是页数
     */
    private int code;
    /**
     * 报文主体
     */
    private Data data;
    /**
     * 信息
     */
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Total{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
