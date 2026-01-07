package com.link.desktop.controller;

import com.link.desktop.entity.DesktopSettings;
import com.link.desktop.service.DesktopSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class DesktopSettingsController {

    @Autowired
    private DesktopSettingsService desktopSettingsService;

    @GetMapping
    public List<DesktopSettings> getAllSettings() {
        return desktopSettingsService.getAllSettings();
    }

    @GetMapping("/map")
    public Map<String, String> getSettingsMap() {
        return desktopSettingsService.getSettingsMap();
    }

    @GetMapping("/{key}")
    public DesktopSettings getSettingByKey(@PathVariable String key) {
        return desktopSettingsService.getSettingByKey(key);
    }

    @PutMapping("/{key}")
    public boolean updateSetting(@PathVariable String key, @RequestParam String value) {
        return desktopSettingsService.updateSetting(key, value);
    }

    @PutMapping
    public boolean updateSettings(@RequestBody Map<String, String> settingsMap) {
        return desktopSettingsService.updateSettings(settingsMap);
    }
}