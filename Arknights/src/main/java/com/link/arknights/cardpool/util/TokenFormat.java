package com.link.arknights.cardpool.util;

import com.alibaba.fastjson2.JSON;

public class TokenFormat {
    public String format(String token) throws Exception {
        // token到手之后格式转换：+ => %2B
        Object parse = JSON.parse(token);
        System.out.println("token为 " + parse.toString());
        return null;
    }
}
