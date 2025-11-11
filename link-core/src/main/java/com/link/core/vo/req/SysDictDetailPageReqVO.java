package com.link.core.vo.req;

import com.link.core.entity.BasePageEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
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
public class SysDictDetailPageReqVO extends BasePageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字典id
     */
    @NotBlank(message = "字典id不能为空")
    private String dictId;

}
