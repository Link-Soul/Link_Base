package com.link.desktop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("desktop_wallpaper")
public class DesktopWallpaper {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String path;
    private String type;
    private Integer isCurrent;
    private Date createTime;
    private Date updateTime;
}