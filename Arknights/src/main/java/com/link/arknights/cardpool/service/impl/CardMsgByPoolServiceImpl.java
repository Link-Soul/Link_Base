package com.link.arknights.cardpool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.link.arknights.cardpool.entity.entityForMessage.CardMsgByPool;
import com.link.arknights.cardpool.mapper.CardMsgByPoolMapper;
import com.link.arknights.cardpool.service.CardMsgByPoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CardMsgByPoolServiceImpl extends ServiceImpl<CardMsgByPoolMapper, CardMsgByPool> implements CardMsgByPoolService {

    @Resource
    private CardMsgByPoolMapper cardMsgByPoolMapper;

}
