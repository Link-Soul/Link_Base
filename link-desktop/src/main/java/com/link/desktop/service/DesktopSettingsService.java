package com.link.desktop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.link.desktop.entity.DesktopSettings;

import java.util.List;
import java.util.Map;

public interface DesktopSettingsService extends IService<DesktopSettings> {
    List<DesktopSettings> getAllSettings();
    DesktopSettings getSettingByKey(String settingKey);
    Map<String, String> getSettingsMap();
    boolean updateSetting(String settingKey, String settingValue);
    boolean updateSettings(Map<String, String> settingsMap);
}