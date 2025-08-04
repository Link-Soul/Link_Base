package com.link.arknights.cardpool.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhouBinBin
 * @Description TODO
 * @since 2025/8/4 14:32
 **/
public class RespUtils {

    public static Map<String, Object> resp(String success, String msg, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", success);
        map.put("msg", msg);
        if (data != null) {
            map.put("data", data);
        }
        return map;
    }
}
