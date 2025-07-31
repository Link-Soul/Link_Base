package com.link.arknights.cardpool.util;

import com.link.arknights.cardpool.entity.getFromArk.Login;
import com.link.arknights.cardpool.entity.getFromArk.TokenByPhonePasswordReq;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Admin {
    public int num = 0;

    public String getToken(TokenByPhonePasswordReq user) throws Exception {
        Map<String, String> headers = new HashMap<>();
//        headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
//        headers.put("Cache-Control","max-age=0");
//        headers.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");

        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9");

        Login login;
        login = new HttpClientExample().HttpPostGetToken("https://as.hypergryph.com/user/auth/v1/token_by_phone_password", headers, user);
        if (Objects.isNull(login) && num < 2) {
            this.num += 1;
            getToken(user);
        }

//        logOut(login.getData().getToken());
        return login.getData().getToken();
    }

    public void logOut(String token) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9");
        Login login = new HttpClientExample().LogOut("https://as.hypergryph.com/user/info/v1/logout", headers, token);
    }
}
