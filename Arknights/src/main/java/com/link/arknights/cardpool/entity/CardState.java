package com.link.arknights.cardpool.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
//import org.hibernate.validator.constraints.Length;

/**
 * 抽卡信息
 *
 * @TableName cardState
 */
@Component
@TableName("cardState")
@Data
public class CardState implements Serializable {

    /**
     *
     */
    @NotNull(message = "[]不能为空")

    @ApiModelProperty("")
    @TableId(type = IdType.AUTO)
    private int id;
    /**
     *
     */
    @Size(max = 10, message = "编码长度不能超过10")
    @ApiModelProperty("")
    private String pool;
    /**
     *
     */
    @Size(max = 20, message = "编码长度不能超过20")
    @ApiModelProperty("")
    private String name;
    /**
     * 下载的数据是星级-1，再添加和使用时直接使用应有的星级
     */
    @ApiModelProperty("下载的数据是星级-1，再添加和使用时直接使用应有的星级")
    private Integer rarity;
    /**
     * 0否，1是
     */
    @ApiModelProperty("0否，1是")
    private Integer isNew;
    /**
     * 即抽卡时间，对应ts
     */
    @ApiModelProperty("即抽卡时间，对应ts")
    private Long createTime;
    /**
     * 是否十连，1为单抽10为十连
     */
    @ApiModelProperty("是否十连，1为单抽10为十连")
    private Integer ifTen;
    /**
     * 角色uid，用于区分
     */
    @NotNull(message = "[角色uid，用于区分]不能为空")
    @ApiModelProperty("角色uid，用于区分")
    private Integer uid;

    @Override
    public String toString() {
        return "CardStat{" +
                "id=" + id +
                ", pool='" + pool + '\'' +
                ", name='" + name + '\'' +
                ", rarity=" + rarity +
                ", isnew=" + isNew +
                ", createTime=" + createTime +
                ", iften=" + ifTen +
                ", uid=" + uid +
                '}';
    }


}
