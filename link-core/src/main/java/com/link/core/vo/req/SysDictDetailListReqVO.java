package com.link.core.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 字典明细
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
public class SysDictDetailListReqVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字典标签
     */
    @NotBlank(message = "字典标签不能为空")
    private String label;
    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空")
    private String dictName;
}
