package com.link.arknights.cardpool.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.link.arknights.cardpool.entity.CardState;
import com.link.arknights.cardpool.entity.entityForMessage.CardMsgByPool;
import com.link.arknights.cardpool.entity.entityForMessage.Role;
import com.link.arknights.cardpool.entity.getFromArk.*;
import com.link.arknights.cardpool.entity.getFromJSON.MyEntity;
import com.link.arknights.cardpool.mapper.CardStateMapper;
import com.link.arknights.cardpool.service.CardStateService;
import com.link.arknights.cardpool.util.Admin;
import com.link.arknights.cardpool.util.HttpClientExample;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Controller
@ResponseBody
public class CardStatController {
    @Resource
    private CardStateService cardStateService;
    @Resource
    private CardStateMapper cardStateMapper;


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
        user.setPhone("13287845587");
        user.setPassword("40580087");
        Admin admin = new Admin();
        String token = admin.getToken(user);
        String formatToken = token.replace("+", "%2B");
        HttpClientExample httpClient = new HttpClientExample();
        String Total = httpClient.GetHttpTotal(1, formatToken, 1);
        com.link.arknights.cardpool.entity.getFromArk.Total parse = JSON.parseObject(Total, Total.class);
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
        boolean i = insertMessage(cardStateList, infoTotal.getData().getUid());
        long time2 = new Date().getTime();
        System.out.println("更新数据共耗时 " + (time2 - time1) + " ms");
        return cardStateList.size();
    }

    @GetMapping("/insertPool")
    public void insertPool() {
        System.out.println("insertPool执行");
        List<String> list = cardStateMapper.getPoolFromCards();
        List<String> poolList = cardStateMapper.getPool();

        // 获取非交集的部分
        List<String> nonIntersection = new ArrayList<>(list);
        nonIntersection.addAll(poolList);  // 将两个列表合并
        // 获取交集部分
        List<String> intersection = new ArrayList<>(list);
        intersection.retainAll(poolList);
        nonIntersection.removeAll(intersection);  // 去除交集部分，剩下的即为非交集的部分
        nonIntersection.removeIf(s -> s.equals(""));
        // 输出非交集的部分
        if (nonIntersection.size() != 0) {
            List<Pool> pools = new ArrayList<>();
            for (String s : nonIntersection) {
                Pool pool = new Pool();
                pool.setPoolName(s);
                pools.add(pool);
            }
            cardStateMapper.insertPoolName(pools);
        }
    }

    /**
     * 通过那个奇怪的json添加  暂时废弃
     *
     * @param json 传入的json
     * @return 是否成功
     * @throws JsonProcessingException
     */
    @PostMapping("/insertFromJSON")
    public int getFromJSON(@RequestBody String json) throws JsonProcessingException {
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
                cardState.setUid(86670999L);
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
                    cardState.setUid(86670999L);
                    list.add(cardState);
                }
            }
        }
        int i = insertNoRepeat(list);
        insertPool();
        return i;
    }

    /**
     * 依照时间戳实现不重复添加
     *
     * @param cardStateList cardStateList
     * @return 影响的行数
     */
    public int insertNoRepeat(List<CardState> cardStateList) {
        if (!Objects.isNull(cardStateList)) {
            List<Long> longs = cardStateMapper.queryAllByCreateTimeLongList();

            for (int i = cardStateList.size() - 1; i >= 0; i--) {
                CardState cardState = cardStateList.get(i);
                Long createTime = cardState.getCreateTime();
                for (Long aLong : longs) {
                    if (aLong.equals(createTime)) {
                        cardStateList.remove(cardState);
                    }
                }
            }
            if (cardStateList.size() != 0) {
                int i = cardStateMapper.insertCards(cardStateList);
                System.out.println("影响的行数为 " + i + "行");
                return i;
            } else {
                System.out.println("影响的行数为 0 行");
                return 0;
            }

        }
        return 0;
    }

    /**
     * 将数据格式化为按卡尺分类的各卡池抽卡信息，并存储到数据库中
     *
     * @param cardMessageByUid 完整的卡池信息
     * @param uid              角色uid
     * @return 是否正确
     */
    public boolean formatCardMsgByPoolList(List<CardState> cardMessageByUid, Long uid) {
        long startTime = new Date().getTime();
        // 获取信息并排序
        List<String> pools = cardStateMapper.getPool();
        int num = 1;
        List<CardState> list = new ArrayList<>();
        for (CardState cardState : cardMessageByUid) {
            cardState.setId(num);
            list.add(cardState);
            num++;
        }

        // 分割池子
        List<List<CardState>> listByPool = new ArrayList<>();
        for (String pool : pools) {
            List<CardState> tempList = new ArrayList<>();
            for (CardState cardState : list) {
                if (Objects.equals(cardState.getPool(), pool)) {
                    tempList.add(cardState);
                }
            }
            listByPool.add(tempList);
        }
        // 数据处理
        List<CardMsgByPool> cardMsgByPoolList = new ArrayList<>();
        for (List<CardState> cardStateList : listByPool) {
            CardMsgByPool cardMsg = new CardMsgByPool();
            cardMsg.setTotal(cardStateList.size());
            cardMsg.setPoolName(cardStateList.get(0).getPool());

            // 抽数计数
            int count = 0;
            for (CardState cardState : cardStateList) {
                count++;
                if (cardState.getRarity() == 6) {
                    Role role = new Role();
                    role.setName(cardState.getName());
                    role.setNum(count);
                    role.setTime(cardState.getCreateTime());
                    count = 0;

                    List<Role> six = cardMsg.getSix();
                    if (Objects.isNull(six)) {
                        List<Role> tempSix = new ArrayList<>();
                        tempSix.add(role);
                        cardMsg.setSix(tempSix);
                    } else {
                        six.add(role);
                        cardMsg.setSix(six);
                    }
                }
            }
            List<Role> six = cardMsg.getSix();
            if (!Objects.isNull(six)) {
                Collections.reverse(six);
                cardMsg.setSix(six);
            }
            cardMsgByPoolList.add(cardMsg);
        }
        // 反转顺序
        Collections.reverse(cardMsgByPoolList);
        long stopTime = new Date().getTime();

        List<Pool> poolList = cardStateMapper.getPoolInfo();
        for (CardMsgByPool cardMsgByPool : cardMsgByPoolList) {

            cardMsgByPool.setUid(uid);
            if (!Objects.isNull(cardMsgByPool.getSix())) {
                for (Pool pool : poolList) {
                    if (cardMsgByPool.getPoolName().equals(pool.getPoolName())) {
                        cardMsgByPool.setUpName1(pool.getUp1());
                        cardMsgByPool.setUpName2(pool.getUp2());
                        cardMsgByPool.setUpUrl1(pool.getUp1Page());
                        cardMsgByPool.setUpUrl2(pool.getUp2Page());
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (Role six : cardMsgByPool.getSix()) {
                    stringBuilder.append(six.getName()).append("&");
                    stringBuilder.append(six.getUrl()).append("&");
                    stringBuilder.append(six.getNum()).append("&");
                    stringBuilder.append(six.getTime()).append(";");
                }
                cardMsgByPool.setTotalSix(stringBuilder.toString());
            }
        }

        // 最终得到的结果。
        System.out.println("将数据格式化为按卡池分类的抽卡信息 处理所需时间为 " + (stopTime - startTime) + " ms");
        return cardStateService.saveOrUpdateCardMsgByPool(cardMsgByPoolList);

    }

    /**
     * 依据最大最小时间插入，速度快，只支持追加，不支持插入
     *
     * @param cardStateList cardStateList
     * @return 影响的行数
     */
    public boolean insertMessage(List<CardState> cardStateList, Long uid) {
        Long date = cardStateMapper.queryBiggestTime();
        List<CardState> cardStateListFormat = new ArrayList<>();
        // 传入数据不为空则是有新添加的数据，需要整合
        if (!Objects.isNull(date)) {
            // 添加数据
            for (CardState cardState : cardStateList) {
                if (cardState.getCreateTime() > date) {
                    cardStateListFormat.add(cardState);
                }
            }
            List<CardState> cardMessageByUid = cardStateMapper.getCardMessageByUid(uid);
            // 有可添加数据则整合然后处理数据。
            if (!cardStateListFormat.isEmpty()) {
                cardMessageByUid.addAll(cardStateListFormat);
                Lock lock = new ReentrantLock();

                lock.lock();
                try {
//                    System.out.println("获取锁1");
                    int i = cardStateMapper.insertCards(cardStateListFormat);
                    System.out.println("受影响的行数 = " + i);
                } finally {
//                    System.out.println("解锁1");
                    lock.unlock();
                }

                lock.lock();
                try {
//                    System.out.println("获取锁6");
                    insertPool();

                } finally {
//                    System.out.println("解锁6");
                    lock.unlock();
                }

                lock.lock();
                try {
//                    System.out.println("获取锁2");
                    boolean b = formatCardMsgByPoolList(cardMessageByUid, uid);
                } finally {
//                    System.out.println("解锁2");
                    lock.unlock();
                }

                return true;
            } else { // 无可添加数据则重新处理一下已有的数据。
                formatCardMsgByPoolList(cardMessageByUid, uid);
                return false;
            }
        } else {  // date为空则是需要全部插入，没有对应的数据。

            Lock lock = new ReentrantLock();

            lock.lock();
            try {
                System.out.println("获取锁3");
                int i = cardStateMapper.insertCards(cardStateListFormat);
                System.out.println("受影响的行数 = " + i);
            } finally {
                System.out.println("解锁3");
                lock.unlock();
            }

            lock.lock();
            try {
                System.out.println("获取锁5");
                insertPool();
            } finally {
                System.out.println("解锁5");
                lock.unlock();
            }

            lock.lock();
            try {
                System.out.println("获取锁4");
                boolean b = formatCardMsgByPoolList(cardStateList, uid);
            } finally {
                System.out.println("解锁4");
                lock.unlock();
            }



/*            int i = cardStateMapper.insertCards(cardStateList);
            formatCardMsgByPoolList(cardStateList,uid);
            System.out.println("受影响的行数 = " + i);*/

            return true;
        }


    }

}
