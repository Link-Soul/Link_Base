package com.link.desktop.controller;

import com.link.desktop.entity.DesktopWallpaper;
import com.link.desktop.service.DesktopWallpaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallpaper")
public class DesktopWallpaperController {

    @Autowired
    private DesktopWallpaperService desktopWallpaperService;

    @GetMapping
    public List<DesktopWallpaper> getAllWallpapers() {
        return desktopWallpaperService.getAllWallpapers();
    }

    @GetMapping("/current")
    public DesktopWallpaper getCurrentWallpaper() {
        return desktopWallpaperService.getCurrentWallpaper();
    }

    @GetMapping("/{id}")
    public DesktopWallpaper getWallpaperById(@PathVariable Long id) {
        return desktopWallpaperService.getWallpaperById(id);
    }

    @PostMapping
    public boolean addWallpaper(@RequestBody DesktopWallpaper wallpaper) {
        return desktopWallpaperService.addWallpaper(wallpaper);
    }

    @PutMapping
    public boolean updateWallpaper(@RequestBody DesktopWallpaper wallpaper) {
        return desktopWallpaperService.updateWallpaper(wallpaper);
    }

    @DeleteMapping("/{id}")
    public boolean deleteWallpaper(@PathVariable Long id) {
        return desktopWallpaperService.deleteWallpaper(id);
    }

    @PutMapping("/current/{id}")
    public boolean setCurrentWallpaper(@PathVariable Long id) {
        return desktopWallpaperService.setCurrentWallpaper(id);
    }
}