package com.link.desktop.entity.constant;

/**
 * 桌面设置配置项枚举
 */
public enum DesktopSettingKey {
    /**
     * 壁纸设置
     */
    WALLPAPER("wallpaper", "壁纸设置"),
    
    /**
     * 桌面图标显示
     */
    SHOW_DESKTOP_ICONS("showDesktopIcons", "桌面图标显示"),
    
    /**
     * 显示网格
     */
    SHOW_GRID("showGrid", "显示网格"),
    
    /**
     * 自动排列
     */
    AUTO_ARRANGE("autoArrange", "自动排列"),
    
    /**
     * 主题模式
     */
    THEME_MODE("themeMode", "主题模式");

    private final String key;
    private final String description;

    DesktopSettingKey(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}