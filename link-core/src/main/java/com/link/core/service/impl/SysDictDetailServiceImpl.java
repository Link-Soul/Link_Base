package com.link.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.link.core.common.redis.RedisRepository;
import com.link.core.common.redis.constant.RedisConstant;
import com.link.core.utils.ValidatorUtils;
import com.link.core.entity.SysDictDetailEntity;
import com.link.core.entity.SysDictEntity;
import com.link.core.mapper.SysDictDetailMapper;
import com.link.core.mapper.SysDictMapper;
import com.link.core.service.SysDictDetailService;
import com.link.core.vo.req.SysDictDetailListReqVO;
import com.link.core.vo.req.SysDictDetailReqVO;
import com.link.core.vo.resp.SysDictDetailListRespVO;
import com.link.core.vo.resp.SysDictDetailRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 数据字典 服务类
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class SysDictDetailServiceImpl extends ServiceImpl<SysDictDetailMapper, SysDictDetailEntity> implements SysDictDetailService {
    @Resource
    private SysDictDetailMapper sysDictDetailMapper;
    @Resource
    private SysDictMapper sysDictMapper;
    @Autowired
    private RedisRepository redisRepository;


    /**
     * 保存
     *
     * @param sysDictDetail sysDictDetail
     */
    @Override
    public void saveDetail(SysDictDetailReqVO sysDictDetail) {
        SysDictEntity sysDictEntity = sysDictMapper.selectById(sysDictDetail.getDictId());
        ValidatorUtils.isExists(sysDictEntity, "字典", sysDictDetail.getDictId());

        LambdaQueryWrapper<SysDictDetailEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysDictDetailEntity::getValue, sysDictDetail.getValue());
        queryWrapper.eq(SysDictDetailEntity::getDictId, sysDictDetail.getDictId());
        SysDictDetailEntity q = getOne(queryWrapper);
        ValidatorUtils.isNull(q, "字典值", sysDictDetail.getValue());

        SysDictDetailEntity entity = new SysDictDetailEntity();
        BeanUtil.copyProperties(sysDictDetail, entity);
        sysDictDetailMapper.insert(entity);
        //删除缓存
        redisRepository.delKeysByNamesPrefix(RedisConstant.CACHE_NAME_DICT_DETAIL + RedisConstant.KEY_SEPARATOR + sysDictEntity.getName());
    }

    /**
     * 删除
     *
     * @param ids      ids
     */
    @Override
    public void deleteDetails(List<String> ids) {
        List<SysDictDetailEntity> dictDetailEntities = listByIds(ids);
        removeByIds(ids);
        //删除缓存
        Optional.ofNullable(dictDetailEntities).orElse(new ArrayList<>()).stream().distinct().forEach(entity -> {
            SysDictEntity sysDictEntity = sysDictMapper.selectById(entity.getDictId());
            if (sysDictEntity != null) {
                redisRepository.delKeysByNamesPrefix(RedisConstant.CACHE_NAME_DICT_DETAIL + RedisConstant.KEY_SEPARATOR + sysDictEntity.getName());
            }
        });
    }

    /**
     * 更新
     *
     * @param sysDictDetail sysDictDetail
     */
    @Override
    public void updateDetail(SysDictDetailEntity sysDictDetail) {
        updateById(sysDictDetail);

        SysDictEntity sysDictEntity = sysDictMapper.selectById(sysDictDetail.getDictId());
        if (sysDictEntity != null) {
            redisRepository.delKeysByNamesPrefix(RedisConstant.CACHE_NAME_DICT_DETAIL + RedisConstant.KEY_SEPARATOR + sysDictEntity.getName());
        }
    }

    @Override
    public IPage<SysDictDetailRespVO> listByPage(Page<SysDictDetailEntity> page, String dictId) {

        SysDictEntity sysDictEntity = sysDictMapper.selectById(dictId);
        ValidatorUtils.isExists(sysDictEntity, "字典", dictId);

        LambdaQueryWrapper<SysDictDetailEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysDictDetailEntity::getDictId, dictId);
        wrapper.orderByAsc(SysDictDetailEntity::getSort);
        IPage<SysDictDetailEntity> result = sysDictDetailMapper.selectPage(page, wrapper);
        IPage<SysDictDetailRespVO> resp = new Page<>();
        BeanUtil.copyProperties(result, resp);
        resp.setRecords(Optional.ofNullable(result.getRecords()).orElse(new ArrayList<>()).stream().map(entity -> {
            SysDictDetailRespVO vo = new SysDictDetailRespVO();
            BeanUtil.copyProperties(entity, vo);
            vo.setDictName(sysDictEntity.getName());
            return vo;
        }).collect(Collectors.toList()));
        return resp;
    }

    /**
     * 查询列表数据，下拉框用
     *
     * @param sysDictDetail sysDictDetail
     * @return List
     */
    @Override
    @Cacheable(cacheNames = RedisConstant.CACHE_NAME_DICT_DETAIL, key = "#sysDictDetail.getDictName() + ':' + #sysDictDetail.getLabel()", cacheManager = "longCacheManager")
    public List<SysDictDetailListRespVO> list(SysDictDetailListReqVO sysDictDetail) {
        SysDictEntity sysDictEntity = sysDictMapper.selectOne(Wrappers.<SysDictEntity>lambdaQuery().eq(SysDictEntity::getName, sysDictDetail.getDictName()));
        ValidatorUtils.isExists(sysDictEntity, "字典", sysDictDetail.getDictName());
        LambdaQueryWrapper<SysDictDetailEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.and(StrUtil.isNotBlank(sysDictDetail.getLabel()),
                res -> res.like(SysDictDetailEntity::getLabel, sysDictDetail.getLabel())
                        .or().like(SysDictDetailEntity::getLabelKr, sysDictDetail.getLabel())
                        .or().like(SysDictDetailEntity::getLabelJp, sysDictDetail.getLabel())
                        .or().like(SysDictDetailEntity::getLabelEn, sysDictDetail.getLabel()));
        wrapper.eq(SysDictDetailEntity::getDictId, sysDictEntity.getId());
        wrapper.orderByAsc(SysDictDetailEntity::getSort);
        List<SysDictDetailEntity> result = sysDictDetailMapper.selectList(wrapper);
        return Optional.ofNullable(result).orElse(new ArrayList<>()).stream().map(entity -> {
            SysDictDetailListRespVO vo = new SysDictDetailListRespVO();
            BeanUtil.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}