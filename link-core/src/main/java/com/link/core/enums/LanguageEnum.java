package com.link.core.enums;

import lombok.Getter;

/**
 *@Description 语言类型枚举
 *@author Link
 *@since 2026/2/2 09:48
 **/
@Getter
public enum LanguageEnum {


    CN("cn", "中文"),
    EN("en", "英文"),
    JP("jp", "日文"),
    KOR("kor", "韩文"),

    ;

    LanguageEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final String code;
    /**
     * 描述
     */
    private final String desc;
}
