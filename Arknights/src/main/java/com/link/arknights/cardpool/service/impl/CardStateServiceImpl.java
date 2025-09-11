package com.link.arknights.cardpool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.link.arknights.cardpool.entity.CardState;
import com.link.arknights.cardpool.entity.entityForMessage.CardMsgByPool;
import com.link.arknights.cardpool.entity.entityForMessage.Role;
import com.link.arknights.cardpool.entity.getFromArk.Pool;
import com.link.arknights.cardpool.mapper.CardMsgByPoolMapper;
import com.link.arknights.cardpool.mapper.CardStateMapper;
import com.link.arknights.cardpool.service.CardStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CardStateServiceImpl extends ServiceImpl<CardStateMapper, CardState> implements CardStateService {

    @Resource
    CardStateMapper cardStateMapper;
    @Resource
    CardMsgByPoolMapper cardMsgByPoolMapper;
    @Resource
    private CardStateService cardStateService;

    @Override
    public List<CardState> getCardMessageByUid(Long uid) {
        return cardStateMapper.getCardMessageByUid(uid);
    }

    /**
     * 更新或新增
     *
     * @param cardMsgByPoolList 按卡池分类的抽卡结果
     * @return 是否
     */
    @Override
    public boolean saveOrUpdateCardMsgByPool(List<CardMsgByPool> cardMsgByPoolList) {
        if (!Objects.isNull(cardMsgByPoolList)) {
            Long uid = cardMsgByPoolList.get(0).getUid();
            List<CardMsgByPool> updateList = new ArrayList<>();
            List<CardMsgByPool> list = cardMsgByPoolMapper.selectByUid(uid);
            if (!Objects.isNull(list)) {
                for (CardMsgByPool cardMsgByPool : list) {
                    for (CardMsgByPool msgByPool : cardMsgByPoolList) {
                        cardMsgByPool.setId(0);
                        msgByPool.setSix(null);
                        // 卡池名相同且信息不同则存入更新列表
                        if (Objects.equals(cardMsgByPool.getPoolName(), msgByPool.getPoolName())
                                && !Objects.equals(cardMsgByPool.getTotalSix(), msgByPool.getTotalSix())) {
                            updateList.add(msgByPool);
                        }
                    }
                }
            }
            //降重
            List<CardMsgByPool> collect = updateList.stream().distinct()
                    .collect(Collectors.toList());
            if (!Objects.isNull(list)) {
                for (CardMsgByPool cardMsgByPool : list) {
                    for (int i = 0; i < cardMsgByPoolList.size(); i++) {
                        if (Objects.equals(cardMsgByPool.getPoolName(), (cardMsgByPoolList.get(i).getPoolName()))) {
                            cardMsgByPoolList.remove(i);
                        }
                    }
                }
            }
            if (collect.size() != 0) {
                for (CardMsgByPool cardMsgByPool : collect) {
                    int b = cardMsgByPoolMapper.updateCardMsgByPool(cardMsgByPool);
                }
            }
            if (cardMsgByPoolList.size() != 0) {
                int i = cardMsgByPoolMapper.insertCardMsgByPool(cardMsgByPoolList);
            }
        }
        return true;
    }


    /**
     * 依照时间戳实现不重复添加
     *
     * @param cardStateList cardStateList
     * @return 影响的行数
     */
    @Override
    public int insertNoRepeat(List<CardState> cardStateList) {
        if (!Objects.isNull(cardStateList)) {
            List<Long> longs = cardStateMapper.queryAllByCreateTimeLongList();

            for (int i = cardStateList.size() - 1; i >= 0; i--) {
                CardState cardState = cardStateList.get(i);
                Long createTime = cardState.getCreateTime();
                for (Long aLong : longs) {
                    if (aLong.equals(createTime)) {
                        // TODO 并发异常
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
    @Override
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
            cardMsg.setStartTime(cardStateList.get(0).getCreateTime());
            cardMsg.setStopTime(cardStateList.get(cardStateList.size() - 1).getCreateTime());
            cardMsgByPoolList.add(cardMsg);
        }
        // 反转顺序
        Collections.reverse(cardMsgByPoolList);
        long stopTime = new Date().getTime();
        // TODO 用上面查到的池子就行，转一下实体
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
                    lock.unlock();
                }

                lock.lock();
                try {
                    insertPool();

                } finally {
                    lock.unlock();
                }

                lock.lock();
                try {
                    boolean b = cardStateService.formatCardMsgByPoolList(cardMessageByUid, uid);
                } finally {
                    lock.unlock();
                }

                return true;
            } else { // 无可添加数据则重新处理一下已有的数据。
                cardStateService.formatCardMsgByPoolList(cardMessageByUid, uid);
                return false;
            }
        } else {  // date为空则是需要全部插入，没有对应的数据。

            Lock lock = new ReentrantLock();

            lock.lock();
            try {
                int i = cardStateMapper.insertCards(cardStateListFormat);
                System.out.println("受影响的行数 = " + i);
            } finally {
                lock.unlock();
            }

            lock.lock();
            try {
                insertPool();
            } finally {
                lock.unlock();
            }

            lock.lock();
            try {
                boolean b = cardStateService.formatCardMsgByPoolList(cardStateList, uid);
            } finally {
                lock.unlock();
            }
/*            int i = cardStateMapper.insertCards(cardStateList);
            formatCardMsgByPoolList(cardStateList,uid);
            System.out.println("受影响的行数 = " + i);*/

            return true;
        }


    }

    public void insertPool() {
        System.out.println("insertPool执行");
        List<Pool> poolListFromCards = cardStateMapper.getPoolFromCards();
        List<String> list = poolListFromCards.stream().map(Pool::getPoolName).collect(Collectors.toList());
        List<String> poolList = cardStateMapper.getPool();

        // 获取非交集的部分
        List<String> nonIntersection = new ArrayList<>(list);
        // 将两个列表合并
        nonIntersection.addAll(poolList);
        // 获取交集部分
        List<String> intersection = new ArrayList<>(list);
        intersection.retainAll(poolList);
        // 去除交集部分，剩下的即为非交集的部分
        nonIntersection.removeAll(intersection);
        nonIntersection.removeIf(s -> s.equals(""));
        // 输出非交集的部分
        if (nonIntersection.size() != 0) {
            List<Pool> pools = new ArrayList<>();
            for (String s : nonIntersection) {
                Pool pool = new Pool();
                pool.setPoolName(s);
                pools.add(pool);
            }
            for (Pool pool : pools) {
                for (Pool one : poolListFromCards) {
                    if (pool.getPoolName().equals(one.getPoolName())){
                        pool.setStartTime(one.getStartTime());
                        pool.setStopTime(one.getStopTime());
                    }
                }
            }
            List<Pool> collect = pools.stream().sorted(Comparator.comparing(Pool::getStartTime)).collect(Collectors.toList());
            cardStateMapper.insertPoolName(collect);
        }
    }
}
