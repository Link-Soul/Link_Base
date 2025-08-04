package com.link.arknights.cardpool.entity.getFromArk;

public class TokenInData {
    private String token;

    @Override
    public String toString() {
        return "TokenInData{" +
                "token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
