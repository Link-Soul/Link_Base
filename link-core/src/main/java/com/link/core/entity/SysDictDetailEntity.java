package com.link.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 字典明细
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dict_detail")
public class SysDictDetailEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字典标签
     */
    private String label;
    /**
     * 字典标签名称（韩语）
     */
    private String labelKr;
    /**
     * 字典标签名称（日语）
     */
    private String labelJp;
    /**
     * 字典标签名称（英语）
     */
    private String labelEn;

    /**
     * 字典值
     */
    private String value;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 字典id
     */
    private String dictId;

}
