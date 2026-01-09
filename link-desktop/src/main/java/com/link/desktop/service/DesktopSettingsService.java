package com.link.desktop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.link.desktop.entity.DesktopSettings;

import java.util.List;
import java.util.Map;



/**
 * 桌面设置服务接口
 * <p>
 * 继承自MyBatis-Plus的IService接口，提供桌面设置相关的业务逻辑方法
 * 用于管理桌面个性化设置，包括获取、更新等操作
 * </p>
 *
 * @author Link
 * @version V1.0
 */
public interface DesktopSettingsService extends IService<DesktopSettings> {
    /**
     * 获取所有桌面设置
     * <p>
     * 查询数据库中所有的桌面设置记录
     * </p>
     *
     * @return 所有桌面设置的列表
     */
    List<DesktopSettings> getAllSettings();

    /**
     * 根据设置键获取桌面设置
     * <p>
     * 通过唯一的设置键查询对应的桌面设置记录
     * </p>
     *
     * @param settingKey 设置键
     * @return 对应的桌面设置对象，如果不存在则返回null
     */
    DesktopSettings getSettingByKey(String settingKey);

    /**
     * 获取桌面设置的键值对映射
     * <p>
     * 将所有桌面设置转换为键值对形式，便于前端直接使用
     * </p>
     *
     * @return 桌面设置的键值对映射
     */
    Map<String, String> getSettingsMap();

    /**
     * 更新单个桌面设置
     * <p>
     * 根据设置键更新对应的设置值
     * </p>
     *
     * @param settingKey   设置键
     * @param settingValue 设置值
     * @return 更新是否成功
     */
    boolean updateSetting(String settingKey, String settingValue);

    /**
     * 批量更新桌面设置
     * <p>
     * 根据键值对映射批量更新多个桌面设置
     * </p>
     *
     * @param settingsMap 桌面设置的键值对映射
     * @return 更新是否成功
     */
    boolean updateSettings(Map<String, String> settingsMap);
}
