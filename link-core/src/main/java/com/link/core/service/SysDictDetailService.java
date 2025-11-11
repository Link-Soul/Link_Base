package com.link.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.link.core.entity.SysDictDetailEntity;
import com.link.core.vo.req.SysDictDetailListReqVO;
import com.link.core.vo.req.SysDictDetailReqVO;
import com.link.core.vo.resp.SysDictDetailListRespVO;
import com.link.core.vo.resp.SysDictDetailRespVO;

import java.util.List;

/**
 * 数据字典 服务类
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
public interface SysDictDetailService extends IService<SysDictDetailEntity> {

    /**
     * 保存
     *
     * @param sysDictDetail sysDictDetail
     */
    void saveDetail(SysDictDetailReqVO sysDictDetail);

    /**
     * 删除
     *
     * @param ids ids
     */
    void deleteDetails(List<String> ids);

    /**
     * 更新
     *
     * @param sysDictDetail sysDictDetail
     */
    void updateDetail(SysDictDetailEntity sysDictDetail);

    /**
     * 分页
     *
     * @param page   page
     * @param dictId dictId
     * @return IPage
     */
    IPage<SysDictDetailRespVO> listByPage(Page<SysDictDetailEntity> page, String dictId);

    /**
     * 查询列表数据，下拉框用
     *
     * @param sysDictDetail sysDictDetail
     * @return List
     */
    List<SysDictDetailListRespVO> list(SysDictDetailListReqVO sysDictDetail);
}

