package com.link.arknights.cardpool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.link.arknights.cardpool.entity.CardState;
import com.link.arknights.cardpool.entity.cardMegForWeb.PoolCount;
import com.link.arknights.cardpool.entity.entityForMessage.RespectiveNum;
import com.link.arknights.cardpool.entity.entityForMessage.getNumByPoolEntity;
import com.link.arknights.cardpool.entity.getFromArk.Pool;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface CardStateMapper extends BaseMapper<CardState> {

    int insertCards(@Param("cardStates") List<CardState> cardStates);

    int insertPoolName(@Param("pools") List<Pool> pools);

    Long queryBiggestTime();

    List<Long> queryAllByCreateTimeLongList();

    List<CardState> getCardMessageByUid(Long uid);


    List<getNumByPoolEntity> getNumByPool();

    List<RespectiveNum> queryRespectiveNum();

    List<Pool> getPoolFromCards();

    List<String> getPool();

    List<Pool> getPoolInfo();
}
