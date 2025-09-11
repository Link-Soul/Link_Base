package com.link.arknights.cardpool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.link.arknights.cardpool.entity.entityForMessage.CardMsgByPool;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CardMsgByPoolMapper extends BaseMapper<CardMsgByPool> {
    int insertCardMsgByPool(@Param("list") List<CardMsgByPool> cardMsgByPoolList);

    List<CardMsgByPool> selectByUid(Long uid);

    int updateCardMsgByPool(CardMsgByPool CardMsgByPool);
}
