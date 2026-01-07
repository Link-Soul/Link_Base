package com.link.desktop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.link.desktop.entity.DesktopWallpaper;
import com.link.desktop.mapper.DesktopWallpaperMapper;
import com.link.desktop.service.DesktopWallpaperService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DesktopWallpaperServiceImpl extends ServiceImpl<DesktopWallpaperMapper, DesktopWallpaper> implements DesktopWallpaperService {

    @Override
    public List<DesktopWallpaper> getAllWallpapers() {
        return list();
    }

    @Override
    public DesktopWallpaper getWallpaperById(Long id) {
        return getById(id);
    }

    @Override
    public DesktopWallpaper getCurrentWallpaper() {
        QueryWrapper<DesktopWallpaper> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_current", 1);
        return getOne(queryWrapper);
    }

    @Override
    public boolean addWallpaper(DesktopWallpaper wallpaper) {
        return save(wallpaper);
    }

    @Override
    public boolean updateWallpaper(DesktopWallpaper wallpaper) {
        return updateById(wallpaper);
    }

    @Override
    public boolean deleteWallpaper(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setCurrentWallpaper(Long id) {
        // 先将所有壁纸设为非当前
        update().set("is_current", 0).update();
        // 再将指定壁纸设为当前
        return update().set("is_current", 1).eq("id", id).update();
    }
}