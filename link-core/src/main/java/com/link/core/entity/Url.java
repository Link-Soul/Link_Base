package com.link.core.entity;

import com.link.core.utils.ToolString;
import lombok.Data;

/**
 *@Description 文件路径的实体
 *@author Link
 *@since 2025/10/17 11:28
 **/
@Data
public class Url {
    /*
     * 总文件路径（格式化后 反斜杠转为斜杠）
     */
    private String totalUrl = "";
    /*
     * 原本的路径
     */
    private String baseUrl = "";
    /*
     * 文件路径（无文件名，最后面有/）
     */
    private String url = "";
    /*
     * 文件名
     */
    private String fileName = "";
    /*
     * 完整文件名，有后缀
     */
    private String fullFileName = "";
    /*
     * 文件后缀
     */
    private String suffix = "";


    public Url(String totalUrl) {
        // 处理空路径
        if (ToolString.isEmpty(totalUrl)) {
            return;
        }

        // 保存原始路径
        this.baseUrl = totalUrl;

        // 格式化路径：统一将反斜杠转为斜杠
        String formattedUrl = totalUrl.replace("\\\\", "\\").replaceAll("\\\\", "/");
        this.totalUrl = formattedUrl;

        // 解析完整文件名（最后一个斜杠后的部分）
        String fullFileName = extractFullFileName(formattedUrl);
        this.fullFileName = fullFileName;

        // 解析目录路径（最后一个斜杠及之前的部分）
        this.url = extractUrl(formattedUrl, fullFileName);

        // 解析文件名和后缀
        parseFileNameAndSuffix(fullFileName);
    }

    /**
     * 提取完整文件名（最后一个斜杠后的部分，无斜杠则为整个字符串）
     */
    private String extractFullFileName(String formattedUrl) {
        int lastSlashIndex = formattedUrl.lastIndexOf("/");
        // 无斜杠或斜杠在末尾（目录路径）
        if (lastSlashIndex == -1 || lastSlashIndex == formattedUrl.length() - 1) {
            return lastSlashIndex == -1 ? formattedUrl : "";
        }
        return formattedUrl.substring(lastSlashIndex + 1);
    }

    /**
     * 提取目录路径（含最后一个斜杠）
     */
    private String extractUrl(String formattedUrl, String fullFileName) {
        if (ToolString.isEmpty(fullFileName)) {
            // 无文件名，整个路径视为目录
            return formattedUrl.endsWith("/") ? formattedUrl : formattedUrl + "/";
        }
        int lastSlashIndex = formattedUrl.lastIndexOf("/");
        return lastSlashIndex == -1 ? "" : formattedUrl.substring(0, lastSlashIndex + 1);
    }

    /**
     * 解析文件名（不含后缀）和后缀
     */
    private void parseFileNameAndSuffix(String fullFileName) {
        if (ToolString.isEmpty(fullFileName)) {
            // 无文件名（目录路径）
            this.fileName = null;
            this.suffix = null;
            return;
        }

        // 处理特殊文件名（以点开头且无其他点，如".gitignore"）
        if (fullFileName.startsWith(".") && fullFileName.indexOf(".") == fullFileName.lastIndexOf(".")) {
            this.fileName = fullFileName; // 整个文件名视为文件名（不含后缀）
            this.suffix = null;
            return;
        }

        // 提取最后一个点的位置（区分后缀）
        int lastDotIndex = fullFileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            // 无后缀（如"readme"）
            this.fileName = fullFileName;
            this.suffix = null;
        } else if (lastDotIndex == 0) {
            // 以点开头但有多个点（如".a.b"）
            this.fileName = fullFileName.substring(0, lastDotIndex); // 空字符串（可根据需求调整）
            this.suffix = fullFileName.substring(lastDotIndex + 1);
        } else {
            // 正常带后缀的文件名（如"image.v1.png"）
            this.fileName = fullFileName.substring(0, lastDotIndex);
            this.suffix = fullFileName.substring(lastDotIndex + 1);
        }
    }
}
