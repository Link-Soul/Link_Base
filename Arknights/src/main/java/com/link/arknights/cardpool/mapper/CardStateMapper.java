package com.link.arknights.cardpool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.link.arknights.cardpool.entity.CardState;
import com.link.arknights.cardpool.entity.cardMegForWeb.PoolCount;
import com.link.arknights.cardpool.entity.entityForMessage.RespectiveNum;
import com.link.arknights.cardpool.entity.entityForMessage.getNumByPoolEntity;
import com.link.arknights.cardpool.entity.getFromArk.Pool;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface CardStateMapper extends BaseMapper<CardState> {

    int insertCards(List<CardState> cardStates);

    Long queryBiggestTime();

    List<Long> queryAllByCreateTimeLongList();

    List<CardState> getCardMessageByUid(Long uid);


    List<PoolCount> getPoolCount();

    List<RespectiveNum> queryRespectiveNum();

    List<getNumByPoolEntity> getNumByPool();

    List<Pool> getPoolFromCards();

    List<String> getPool();

    int insertPoolName(List<Pool> pools);


    List<Pool> getPoolInfo();
}
