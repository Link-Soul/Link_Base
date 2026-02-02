package com.link.core.utils;


import cn.hutool.core.util.StrUtil;
import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 *@Description Lingua 翻译工具类，主要用来检测语言类型，中英日韩？是哪国语言
 *@author Link
 *@since 2026/1/31 09:23
 **/
@Slf4j
public class LinguaUtils {

    // 全局单例检测器，项目启动时初始化，项目结束时通过 LinguaResourceDestroyer 类释放资源
    private static volatile LanguageDetector LANG_DETECTOR;

    // 最小相对距离：默认0.00，可根据业务调整（0.00-0.99，值越高检测越严格，短文本不建议高值）
    private static final float MIN_RELATIVE_DISTANCE = 0.00f;


    // 私有化构造器，禁止外部实例化
    private LinguaUtils() {
    }

    /**
     * 初始化检测器
     */
    private static LanguageDetector getDetector() {
        if (LANG_DETECTOR == null) {
            synchronized (LinguaUtils.class) {
                if (LANG_DETECTOR == null) {
                    // 按官方API构建：指定检测语言+最小相对距离
                    LANG_DETECTOR = LanguageDetectorBuilder
                            .fromLanguages(Language.ENGLISH, Language.CHINESE, Language.JAPANESE, Language.KOREAN)
                            .withMinimumRelativeDistance(MIN_RELATIVE_DISTANCE)
                            // 可选：预加载模型（避免首次调用延迟，适合应用服务器）
                            .withPreloadedLanguageModels()
                            // 可选：低精度模式（节省内存，长文本推荐，短文本准确率下降）
                            // .withLowAccuracyMode()
                            .build();
                    log.info("Lingua翻译包已初始化，检测语言: {}", LANG_DETECTOR.getLanguages$lingua());
                }
            }
        }
        return LANG_DETECTOR;
    }


    /**
     * 检测文本语言
     *
     * @param text 待检测文本
     * @return 检测到的语言，无匹配/空文本返回null
     */
    public static Language detectLanguage(String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        try {
            return LANG_DETECTOR.detectLanguageOf(text);
        } catch (IllegalArgumentException e) {
            // 捕获所有检测异常，返回null
            return null;
        }
    }

    /**
     * 计算文本的语言置信度值，也就是看可能是什么语言。最可能的语言位于keySet的第一位
     *  （按置信度降序排列）
     * @param text 待检测文本
     * @return 语言-置信度映射表（0.0-1.0，最可能的语言为1.0，无匹配返回空Map）
     */
    public static Map<Language, Double> computeConfidence(String text) {
        if (StrUtil.isBlank(text)) {
            // 空文本返回null，需要调用方处理
            return null;
        }
        try {
            return getDetector().computeLanguageConfidenceValues(text);
        } catch (IllegalArgumentException e) {
            // 空文本返回null，需要调用方处理
            return null;
        }
    }

    /**
     * 彻底销毁检测器
     * 调用后再次检测会重新初始化检测器
     */
    public static void destroyDetector() {
        if (LANG_DETECTOR != null) {
            LANG_DETECTOR.unloadLanguageModels();
        }
        LANG_DETECTOR = null;
    }

    public static void main(String[] args) {
        // 测试文本
        String testText = "你好，我是一个中国人。";

        // 初始化检测器
        LanguageDetector detector = getDetector();

        // 检测语言
        Language detectedLanguage = detectLanguage(testText);
        System.out.println("检测到的语言: " + detectedLanguage);

        // 计算置信度
        Map<Language, Double> confidenceMap = computeConfidence(testText);
        System.out.println("语言置信度: " + confidenceMap);
    }
}
