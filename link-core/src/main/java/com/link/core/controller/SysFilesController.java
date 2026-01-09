package com.link.core.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.link.core.entity.BasePageEntity;
import com.link.core.entity.SysFilesEntity;
import com.link.core.service.SysFilesService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 文件上传
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/sysFiles")
public class SysFilesController {
    @Resource
    private SysFilesService sysFilesService;

    @PostMapping
//    @SaCheckPermission(value = {"sysFiles:add", "sysContent:update", "sysContent:add", "WaKaGao1688:index"}, mode = SaMode.OR)
    @SaIgnore
//    @RateLimit(limit = 1)
    public String add(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
        return sysFilesService.saveFile(file, request);
    }

    @DeleteMapping
    @SaCheckPermission("sysFiles:delete")
    public void delete(@RequestBody List<String> ids) {
        sysFilesService.removeByIdsAndFiles(ids);
    }

    @PostMapping("/listByPage")
    @SaCheckPermission("sysFiles:list")
    public IPage<SysFilesEntity> findListByPage(@RequestBody BasePageEntity sysFiles) {
        return sysFilesService.page(sysFiles.getQueryPage(), Wrappers.<SysFilesEntity>lambdaQuery().orderByDesc(SysFilesEntity::getCreateTime));
    }


}
