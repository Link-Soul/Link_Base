-- 创建desktop数据库
CREATE DATABASE IF NOT EXISTS desktop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE desktop;

-- 备忘录表
CREATE TABLE IF NOT EXISTS desktop_notes (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    title VARCHAR(255) NOT NULL COMMENT '标题',
    content TEXT COMMENT '内容',
    status TINYINT DEFAULT 0 COMMENT '状态：0-未完成，1-已完成，2-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='桌面备忘录表';

-- 壁纸表
CREATE TABLE IF NOT EXISTS desktop_wallpaper (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(255) NOT NULL COMMENT '壁纸名称',
    path VARCHAR(500) NOT NULL COMMENT '壁纸路径',
    type VARCHAR(50) DEFAULT 'local' COMMENT '壁纸类型：local-本地，online-在线',
    is_current TINYINT DEFAULT 0 COMMENT '是否当前使用：0-否，1-是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='桌面壁纸表';

-- 个性化设置表
CREATE TABLE IF NOT EXISTS desktop_settings (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    setting_key VARCHAR(100) NOT NULL COMMENT '设置键',
    setting_value VARCHAR(500) COMMENT '设置值',
    setting_type VARCHAR(50) DEFAULT 'string' COMMENT '设置类型：string-字符串，number-数字，boolean-布尔，json-JSON',
    description VARCHAR(255) COMMENT '设置描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_setting_key (setting_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='桌面个性化设置表';

-- 初始化默认设置
INSERT INTO desktop_settings (setting_key, setting_value, setting_type, description) VALUES
('theme', 'light', 'string', '主题：light-浅色，dark-深色'),
('language', 'zh-CN', 'string', '语言设置'),
('dock_position', 'bottom', 'string', '任务栏位置：top-顶部，bottom-底部，left-左侧，right-右侧'),
('auto_hide_dock', 'false', 'boolean', '是否自动隐藏任务栏'),
('wallpaper_rotation', 'false', 'boolean', '是否开启壁纸轮换'),
('wallpaper_rotation_interval', '3600', 'number', '壁纸轮换间隔（秒）');
