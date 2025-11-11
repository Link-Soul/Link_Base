package com.link.core.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 字典管理
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
public class SysDictReqVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字典名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 备注
     */
    private String remark;


}
