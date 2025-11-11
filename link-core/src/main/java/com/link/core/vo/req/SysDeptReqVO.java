package com.link.core.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 新增/修改部门
 *
 * @author Link
 * @version V1.0
 * @since 2020年3月18日
 */
@Data
public class SysDeptReqVO implements Serializable {

    /**
     * 部门名称
     */
    @NotBlank(message = "机构名称不能为空")
    private String name;

    /**
     * 父级部门ID
     */
    @NotBlank(message = "父级不能为空")
    private String pid;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 部门经理名称
     */
    private String managerName;

    /**
     * 部门经理手机
     */
    private String phone;

}