package com.link.desktop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.link.desktop.entity.DesktopWallpaper;

import java.util.List;

public interface DesktopWallpaperService extends IService<DesktopWallpaper> {
    List<DesktopWallpaper> getAllWallpapers();
    DesktopWallpaper getWallpaperById(Long id);
    DesktopWallpaper getCurrentWallpaper();
    boolean addWallpaper(DesktopWallpaper wallpaper);
    boolean updateWallpaper(DesktopWallpaper wallpaper);
    boolean deleteWallpaper(Long id);
    boolean setCurrentWallpaper(Long id);
}