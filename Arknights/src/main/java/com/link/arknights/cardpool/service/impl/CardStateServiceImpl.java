package com.link.arknights.cardpool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.link.arknights.cardpool.entity.CardState;
import com.link.arknights.cardpool.entity.entityForMessage.CardMsgByPool;
import com.link.arknights.cardpool.mapper.CardMsgByPoolMapper;
import com.link.arknights.cardpool.mapper.CardStateMapper;
import com.link.arknights.cardpool.service.CardStateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CardStateServiceImpl extends ServiceImpl<CardStateMapper, CardState> implements CardStateService {

    @Resource
    CardStateMapper cardStateMapper;
    @Resource
    CardMsgByPoolMapper cardMsgByPoolMapper;

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
}
