package com.link.arknights.test;

import com.link.core.enums.LanguageEnum;
import com.link.core.utils.BaiduTranslationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *@Description 测试百度翻译
 *@author Link
 *@since 2026/2/2 10:02
 **/
@SpringBootTest
public class TestBaiduTranslation {

    @Test
    public void testTranslation(){
        String result = BaiduTranslationUtils.translation("篮球", LanguageEnum.JP);
        System.out.println("result = " + result);
    }

}
