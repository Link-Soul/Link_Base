package com.link.core.vo.req;

import com.link.core.entity.BasePageEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 字典管理
 *
 * @author Link
 * @version V1.0
 * @date 2020年3月18日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictPageReqVO extends BasePageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字典名称
     */
    private String name;


}
