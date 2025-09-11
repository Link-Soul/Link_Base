package com.link.arknights.cardpool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.link.arknights.cardpool.entity.CardState;
import com.link.arknights.cardpool.entity.entityForMessage.CardMsgByPool;

import java.util.List;


public interface CardStateService extends IService<CardState> {

    List<CardState> getCardMessageByUid(Long uid);

    boolean saveOrUpdateCardMsgByPool(List<CardMsgByPool> cardMsgByPoolList);

    int insertNoRepeat(List<CardState> cardStateList);

    boolean formatCardMsgByPoolList(List<CardState> cardMessageByUid, Long uid);

    boolean insertMessage(List<CardState> cardStateList, Long uid);

    void insertPool();
}
