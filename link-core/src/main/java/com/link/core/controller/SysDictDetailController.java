package com.link.core.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import com.link.core.entity.SysDictDetailEntity;
import com.link.core.service.SysDictDetailService;
import com.link.core.vo.req.SysDictDetailListReqVO;
import com.link.core.vo.req.SysDictDetailPageReqVO;
import com.link.core.vo.req.SysDictDetailReqVO;
import com.link.core.vo.req.SysDictDetailUpdateReqVO;
import com.link.core.vo.resp.SysDictDetailListRespVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


/**
 * 字典明细管理
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/sysDictDetail")
public class SysDictDetailController {
    @Resource
    private SysDictDetailService sysDictDetailService;

    @PostMapping
    public void add(@RequestBody @Valid SysDictDetailReqVO sysDictDetail) {
        sysDictDetailService.saveDetail(sysDictDetail);
    }

    @DeleteMapping
    public void delete(@RequestBody List<String> ids) {
        sysDictDetailService.deleteDetails(ids);
    }

    @PutMapping("{id}")
    public void update(@PathVariable("id") String id, @RequestBody @Valid SysDictDetailUpdateReqVO sysDictDetail) {
        SysDictDetailEntity entity = new SysDictDetailEntity();
        BeanUtil.copyProperties(sysDictDetail, entity);
        entity.setId(id);
        sysDictDetailService.updateDetail(entity);
    }


    @PostMapping("/listByPage")
    public IPage<SysDictDetailEntity> findListByPage(@RequestBody SysDictDetailPageReqVO sysDictDetail) {
        if (StrUtil.isEmpty(sysDictDetail.getDictId())) {
            return new Page<>();
        }
        return sysDictDetailService.listByPage(sysDictDetail.getQueryPage(), sysDictDetail.getDictId());
    }

    @GetMapping("/list")
    public List<SysDictDetailListRespVO> list(@Valid SysDictDetailListReqVO sysDictDetail) {
        return sysDictDetailService.list(sysDictDetail);
    }
}
