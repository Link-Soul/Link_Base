package com.link.entity;

import com.alibaba.fastjson2.JSONObject;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Author zhoubinbin
 * @Date 2025/4/12 15:22
 */
public class RequestTask {
    public final JSONObject param;
    public final CompletableFuture<Map<String, Object>> future;
    public RequestTask(JSONObject param, CompletableFuture<Map<String, Object>> future) {
        this.param = param;
        this.future = future;
    }
}
