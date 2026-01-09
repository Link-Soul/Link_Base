package com.link.desktop.controller;

import com.link.desktop.entity.DesktopSettings;
import com.link.desktop.service.DesktopSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/settings")
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

    @PutMapping("/updateSetting")
    public boolean updateSetting(@RequestBody DesktopSettings setting) {
        return desktopSettingsService.updateSetting(setting.getSettingKey(), setting.getSettingValue());
    }

    @PutMapping("/updateSettings")
    public boolean updateSettings(@RequestBody Map<String, String> settingsMap) {
        return desktopSettingsService.updateSettings(settingsMap);
    }
}