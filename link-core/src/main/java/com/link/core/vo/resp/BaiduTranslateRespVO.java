package com.link.core.vo.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 *@Description 百度翻译返回值封装
 *@author Link
 *@since 2026/2/2 11:05
 **/
@Data
public class BaiduTranslateRespVO {


    /**
     * 翻译核心结果
     */
    @JsonProperty("result")
    private TranslateResult result;

    /**
     * 百度接口日志ID（用于问题排查/接口调试）
     */
    @JsonProperty("log_id")
    private Long logId;

    /**
     * 内部类：翻译核心结果（对应result节点）
     */
    @Data
    public static class TranslateResult {
        /**
         * 源语言编码（如zh=中文）
         */
        @JsonProperty("from")
        private String from;

        /**
         * 目标语言编码（如jp=日语）
         */
        @JsonProperty("to")
        private String to;

        /**
         * 翻译结果列表（支持多段文本翻译，故为List）
         */
        @JsonProperty("trans_result")
        private List<TranslateItem> transResult;
    }

    /**
     * 内部类：单条翻译结果（对应trans_result数组项）
     */
    @Data
    public static class TranslateItem {
        /**
         * 翻译后的目标文本
         */
        @JsonProperty("dst")
        private String dst;

        /**
         * 翻译前的源文本
         */
        @JsonProperty("src")
        private String src;
    }
}
