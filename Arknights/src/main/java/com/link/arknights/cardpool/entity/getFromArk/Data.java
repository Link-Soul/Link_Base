package com.link.arknights.cardpool.entity.getFromArk;

import java.util.List;

@lombok.Data
public class Data {
    /**
     * 抽卡信息
     */
    private List<DataList> list;
    /**
     * Page信息
     */
    private Pagination pagination;
    /**
     * 接收token时使用Total实体，里面的data只包含content字段
     */
    private String content;
}
