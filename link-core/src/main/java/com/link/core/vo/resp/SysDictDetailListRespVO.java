package com.link.core.vo.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * 字典明细
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
public class SysDictDetailListRespVO implements Serializable {
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

}
