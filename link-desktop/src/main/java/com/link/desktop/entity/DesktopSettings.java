package com.link.desktop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 桌面设置实体类
 */
@Data
@TableName("desktop_settings")
public class DesktopSettings {
    
    /**
     * 设置键
     */
    @TableId
    private String settingKey;
    
    /**
     * 设置值
     */
    private String settingValue;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
}