package com.link.arknights.cardpool.entity.getFromArk;

import java.util.List;

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

    public List<DataList> getList() {
        return list;
    }

    public void setList(List<DataList> list) {
        this.list = list;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Data{" +
                "list=" + list +
                ", pagination=" + pagination +
                ", content='" + content + '\'' +
                '}';
    }
}
