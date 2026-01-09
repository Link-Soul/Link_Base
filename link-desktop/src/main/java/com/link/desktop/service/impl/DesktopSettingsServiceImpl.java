package com.link.desktop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.link.desktop.entity.DesktopSettings;
import com.link.desktop.mapper.DesktopSettingsMapper;
import com.link.desktop.service.DesktopSettingsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DesktopSettingsServiceImpl extends ServiceImpl<DesktopSettingsMapper, DesktopSettings> implements DesktopSettingsService {

    @Override
    public List<DesktopSettings> getAllSettings() {
        return list();
    }

    @Override
    public DesktopSettings getSettingByKey(String settingKey) {
        QueryWrapper<DesktopSettings> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setting_key", settingKey);
        return getOne(queryWrapper);
    }

    @Override
    public Map<String, String> getSettingsMap() {
        List<DesktopSettings> settingsList = list();
        Map<String, String> settingsMap = new HashMap<>();
        for (DesktopSettings setting : settingsList) {
            settingsMap.put(setting.getSettingKey(), setting.getSettingValue());
        }
        return settingsMap;
    }

    @Override
    public boolean updateSetting(String settingKey, String settingValue) {
        LambdaQueryWrapper<DesktopSettings> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DesktopSettings::getSettingKey, settingKey);
        DesktopSettings setting = getOne(queryWrapper);
        if (setting == null) {
            setting = new DesktopSettings();
            setting.setSettingKey(settingKey);
        }
        setting.setSettingValue(settingValue);
        return saveOrUpdate(setting);
    }

    @Override
    public boolean updateSettings(Map<String, String> settingsMap) {
        boolean result = true;
        for (Map.Entry<String, String> entry : settingsMap.entrySet()) {
            boolean updateResult = updateSetting(entry.getKey(), entry.getValue());
            if (!updateResult) {
                result = false;
            }
        }
        return result;
    }
}