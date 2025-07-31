package com.link.arknights.cardpool.util;

public class GetToken {
    public String getToken() throws Exception {
        HttpClientExample clientExample = new HttpClientExample();
        String Total = clientExample.GetHttpToken();//报文
        TokenFormat tokenFormat = new TokenFormat();
        return tokenFormat.format(Total);
    }
}
