package com.link.arknights.cardpool.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.link.arknights.cardpool.entity.CardState;
import com.link.arknights.cardpool.entity.getFromArk.*;
import com.link.arknights.cardpool.entity.getFromJSON.MyEntity;
import com.link.arknights.cardpool.service.CardStateService;
import com.link.arknights.cardpool.util.Admin;
import com.link.arknights.cardpool.util.HttpClientExample;
import com.link.core.utils.RespUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@ResponseBody
public class CardStatController {

    @Resource
    private CardStateService cardStateService;


    @Value(value = "${ark.userId}")
    private String userId;
    @Value(value = "${ark.password}")
    private String password;


    /**
     * 从官网获取寻访数据并进行添加
     *
     * @param response 没用上
     * @return 是否成功
     */
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public synchronized int insert(HttpServletResponse response) throws Exception {
        long time1 = new Date().getTime();
        TokenByPhonePasswordReq user = new TokenByPhonePasswordReq();
        user.setPhone(userId);
        user.setPassword(password);
        Admin admin = new Admin();
        String token = admin.getToken(user);
        String formatToken = token.replace("+", "%2B");
        HttpClientExample httpClient = new HttpClientExample();
        String Total = httpClient.GetHttpTotal(1, formatToken, 1);
        Total parse = JSON.parseObject(Total, Total.class);
        // 总页数 73 => 8
//        int total = (parse.getData().getPagination().getTotal() / 10 ) + 1;
        int total = parse.getData().getPagination().getTotal() % 10 == 0 ?
                parse.getData().getPagination().getTotal() / 10 : (parse.getData().getPagination().getTotal() / 10) + 1;
        List<Total> list = new ArrayList<>();
        List<CardState> cardStateList = new ArrayList<>();
        String info = httpClient.HttpPost("https://as.hypergryph.com/u8/user/info/v1/basic", token);
        InfoTotal infoTotal = JSON.parseObject(info, InfoTotal.class);
//        System.out.println(parse);
        for (int i = 1; i <= total; i++) {
            list.add(JSON.parseObject(httpClient.GetHttpTotal(i, formatToken, 1), Total.class));
        }
        for (Total a : list) {
            if (!Objects.isNull(a)) {
                for (DataList dataList : a.getData().getList()) {
                    if (dataList.getChars().size() == 10) {
                        Collections.reverse(dataList.getChars());
                        for (Single aChar : dataList.getChars()) {
                            CardState cardState = new CardState();
                            cardState.setPool(dataList.getPool());
                            cardState.setName(aChar.getName());
                            cardState.setRarity(aChar.getRarity() + 1);
                            cardState.setIsNew(aChar.getIsNew().equals("true") ? 1 : 0);
                            cardState.setCreateTime(dataList.getTs());
                            cardState.setIfTen(dataList.getChars().size());
                            cardState.setUid(infoTotal.getData().getUid());
                            cardStateList.add(cardState);
                        }
                    } else {
                        CardState cardState = new CardState();
                        cardState.setPool(dataList.getPool());
                        cardState.setName(dataList.getChars().get(0).getName());
                        cardState.setRarity(dataList.getChars().get(0).getRarity() + 1);
                        cardState.setIsNew(dataList.getChars().get(0).getIsNew().equals("true") ? 1 : 0);
                        cardState.setCreateTime(dataList.getTs());
                        cardState.setIfTen(dataList.getChars().size());
                        cardState.setUid(infoTotal.getData().getUid());
                        cardStateList.add(cardState);
                    }
                }
            }

        }
        admin.logOut(token);
        Collections.reverse(cardStateList);
        boolean i = cardStateService.insertMessage(cardStateList, Long.valueOf(infoTotal.getData().getUid()));
        long time2 = new Date().getTime();
        System.out.println("更新 " + i + " 条数据，共耗时 " + (time2 - time1) + " ms");
        return cardStateList.size();
    }

    /**
     * 小黑盒导入抽卡数据
     *
     * @param fileUrl 传入的文件url
     * @return 是否成功
     * @throws JsonProcessingException
     */
    @PostMapping("/insertFromXhhWeb")
    public Map<String, Object> insertFromXhhWeb(String fileUrl) {
        log.info("从小黑盒导入抽卡数据,输入url：{}", fileUrl);
        StringBuilder content = new StringBuilder();
        HttpURLConnection connection = null;
        // 一、下载文件
        try {
            // 1. 创建URL连接
            URL url = new URL(fileUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // 连接超时时间
            connection.setConnectTimeout(5000);
            // 读取超时时间
            connection.setReadTimeout(5000);
            // 2. 检查响应状态（200表示成功）
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 3. 获取输入流并读取内容
                try (InputStream is = connection.getInputStream();
                     BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

                    content = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }
            } else {
                log.error("下载失败，响应码：" + connection.getResponseCode());
                return RespUtils.resp("false", "下载失败", null);
            }
        } catch (Exception e) {
            log.error("文件下载失败：{}", e.getMessage());
            return RespUtils.resp("false", "文件下载失败", null);
        } finally {
            if (connection != null) {
                connection.disconnect(); // 关闭连接
            }
        }
        // 二、转json

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = JSONObject.parseObject(content.toString());
        } catch (Exception e) {
            log.error("json转换错误", e);
            return RespUtils.resp("false", "json转换错误", null);
        }
        if (jsonObject != null && jsonObject.containsKey("data") && jsonObject.containsKey("info")) {
            String data = jsonObject.getString("data");
            JSONObject info = jsonObject.getJSONObject("info");
            Integer uid = info.getInteger("uid");
            try {
                log.info("用户uid:{}，执行数据入库操作。导入文件url：{}", uid, fileUrl);
                int count = getFromJSON(data, uid);
                return RespUtils.resp("true", "导入成功", "影响行数=" + count);
            } catch (Exception e) {
                log.error("文件入库失败", e);
                return RespUtils.resp("false", "文件入库失败", null);
            }
        } else {
            return RespUtils.resp("false", "文件解析失败", null);
        }
    }


    /**
     * 通过那个奇怪的json添加
     *
     * @param json 传入的json
     * @return 是否成功
     */
//    @PostMapping("/insertFromJSON")
    public int getFromJSON(@RequestBody String json, Integer uid) {
        Map<String, MyEntity> entityMap = JSONObject.parseObject(json, new TypeReference<Map<String, MyEntity>>() {
        });
        List<CardState> list = new ArrayList<>();
        for (Map.Entry<String, MyEntity> entry : entityMap.entrySet()) {
            String key = entry.getKey();
            MyEntity value = entry.getValue();
            String p = value.getP();
            List<List<String>> c = value.getC();
            if (c.size() == 1) {
                CardState cardState = new CardState();
                String name = c.get(0).get(0);
                String rarity = c.get(0).get(1);
                String isNew = c.get(0).get(2);
                cardState.setPool(p);
                cardState.setName(name);
                cardState.setRarity(Integer.parseInt(rarity) + 1);
                cardState.setIsNew(Integer.valueOf(isNew));
                cardState.setCreateTime(Long.valueOf(key));
                cardState.setIfTen(1);
                cardState.setUid(uid);
                list.add(cardState);
            } else if (c.size() == 10) {
                for (List<String> strings : c) {
                    CardState cardState = new CardState();
                    String name = strings.get(0);
                    String rarity = strings.get(1);
                    String isNew = strings.get(2);
                    cardState.setPool(p);
                    cardState.setName(name);
                    cardState.setRarity(Integer.parseInt(rarity) + 1);
                    cardState.setIsNew(Integer.valueOf(isNew));
                    cardState.setCreateTime(Long.valueOf(key));
                    cardState.setIfTen(10);
                    cardState.setUid(uid);
                    list.add(cardState);
                }
            }
        }
        List<CardState> collect = list.stream().sorted(Comparator.comparing(CardState::getCreateTime)).collect(Collectors.toList());
        List<CardState> collectList = new ArrayList<>(collect);
        int i;
        synchronized (this) {
            i = cardStateService.insertNoRepeat(collect);
            cardStateService.insertPool();
        }
        try {
            cardStateService.formatCardMsgByPoolList(collectList, Long.valueOf(uid));
        } catch (Exception e) {
            log.error("数据处理错误", e);
        }

        return i;
    }

}
