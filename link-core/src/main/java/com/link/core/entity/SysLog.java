package com.link.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
@TableName("sys_log")
public class SysLog implements Serializable {
    @TableId
    private String id;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String userId;

    private String username;

    private String operation;

    private Integer time;

    private String method;

    private String params;

    private String ip;
    /**
     * 组织关系
     */
    private String organRelation;
}