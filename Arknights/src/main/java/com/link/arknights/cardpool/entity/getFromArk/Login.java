package com.link.arknights.cardpool.entity.getFromArk;

/**
 * 登陆获取token时的实体
 */
public class Login {
    private int status;
    private String type;
    private String msg;
    private TokenInData data;

    @Override
    public String toString() {
        return "Login{" +
                "status=" + status +
                ", type='" + type + '\'' +
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TokenInData getData() {
        return data;
    }

    public void setData(TokenInData data) {
        this.data = data;
    }
}
