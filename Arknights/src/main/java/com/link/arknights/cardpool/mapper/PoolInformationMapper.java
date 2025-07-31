package com.link.arknights.cardpool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.link.arknights.cardpool.entity.PoolInformation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PoolInformationMapper extends BaseMapper<PoolInformation> {
    int savePool(PoolInformation pool);
}
