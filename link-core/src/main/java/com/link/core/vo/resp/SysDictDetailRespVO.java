package com.link.core.vo.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典明细
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
public class SysDictDetailRespVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
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
     * 字典名称
     */
    private String dictName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 字典id
     */
    private String dictId;

    private Date createTime;

    private Date updateTime;

}
