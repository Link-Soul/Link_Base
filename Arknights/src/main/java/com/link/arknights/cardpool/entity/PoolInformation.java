package com.link.arknights.cardpool.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class PoolInformation {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String poolName;
    private String up1;
    private String up2;
    private String up1Page;
    private String up2Page;
    private String state;
    private String startTime;
    private String stopTime;
}
