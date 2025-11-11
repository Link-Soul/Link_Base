package com.link.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *@Description TODO
 *@author Link
 *@since 2025/11/10 17:52
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_files")
public class SysFilesEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * URL地址
     */
    private String url;

    private String fileName;

    private String filePath;
    /**
     * 组织关系
     */
    private String organRelation;
}
