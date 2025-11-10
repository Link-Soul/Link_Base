package com.link.core.utils;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * @author Link
 * @Description 字符串处理常用方法
 * @since 2025/9/11 14:24
 **/
public abstract class ToolString {

    // 正则表达式使用方式： abc.matches(ToolString.regExp_integer_1)
    // 或者使用 regExpVali 方法

    /**
     * 常用正则表达式：匹配非负整数（正整数 + 0）
     */
    public final static String regExp_integer_1 = "^\\d+$";

    /**
     * 常用正则表达式：匹配正整数
     */
    public final static String regExp_integer_2 = "^[0-9]*[1-9][0-9]*$";

    /**
     * 常用正则表达式：匹配非正整数（负整数 + 0）
     */
    public final static String regExp_integer_3 = "^((-\\d+) ?(0+))$";

    /**
     * 常用正则表达式：匹配负整数
     */
    public final static String regExp_integer_4 = "^-[0-9]*[1-9][0-9]*$";

    /**
     * 常用正则表达式：匹配整数
     */
    public final static String regExp_integer_5 = "^-?\\d+$";

    /**
     * 常用正则表达式：匹配非负浮点数（正浮点数 + 0）
     */
    public final static String regExp_float_1 = "^\\d+(\\.\\d+)?$";

    /**
     * 常用正则表达式：匹配正浮点数
     */
    public final static String regExp_float_2 = "^(([0-9]+\\.[0-9]*[1-9][0-9]*) ?([0-9]*[1-9][0-9]*\\.[0-9]+) ?([0-9]*[1-9][0-9]*))$";

    /**
     * 常用正则表达式：匹配非正浮点数（负浮点数 + 0）
     */
    public final static String regExp_float_3 = "^((-\\d+(\\.\\d+)?) ?(0+(\\.0+)?))$";

    /**
     * 常用正则表达式：匹配负浮点数
     */
    public final static String regExp_float_4 = "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*) ?([0-9]*[1-9][0-9]*\\.[0-9]+) ?([0-9]*[1-9][0-9]*)))$";

    /**
     * 常用正则表达式：匹配浮点数
     */
    public final static String regExp_float_5 = "^(-?\\d+)(\\.\\d+)?$";

    /**
     * 常用正则表达式：匹配由26个英文字母组成的字符串
     */
    public final static String regExp_letter_1 = "^[A-Za-z]+$";

    /**
     * 常用正则表达式：匹配由26个英文字母的大写组成的字符串
     */
    public final static String regExp_letter_2 = "^[A-Z]+$";

    /**
     * 常用正则表达式：匹配由26个英文字母的小写组成的字符串
     */
    public final static String regExp_letter_3 = "^[a-z]+$";

    /**
     * 常用正则表达式：匹配由0-9组成的字符串
     */
    public final static String regExp_letter_6 = "^[0-9]+$";
    /**
     * 常用正则表达式：匹配由0-9或-组成的字符串
     */
    public final static String regExp_letter_7 = "^[0-9-]+$";

    /**
     * 常用正则表达式：匹配由数字和26个英文字母组成的字符串
     */
    public final static String regExp_letter_4 = "^[A-Za-z0-9]+$";

    /**
     * 常用正则表达式：匹配由数字、26个英文字母或者下划线组成的字符串
     */
    public final static String regExp_letter_5 = "^\\w+$";

    /**
     * 常用正则表达式：匹配email地址
     */
    public final static String regExp_email = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";

    /**
     * 常用正则表达式：匹配url
     */
    public final static String regExp_url_1 = "^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$";

    /**
     * 常用正则表达式：匹配url
     */
    public final static String regExp_url_2 = "[a-zA-z]+://[^\\s]*";

    /**
     * 常用正则表达式：匹配中文字符
     */
    public final static String regExp_chinese_1 = "[\\u4e00-\\u9fa5]";

    /**
     * 常用正则表达式：匹配双字节字符(包括汉字在内)
     */
    public final static String regExp_chinese_2 = "[^\\x00-\\xff]";

    /**
     * 常用正则表达式：匹配空行
     */
    public final static String regExp_line = "\\n[\\s ? ]*\\r";

    /**
     * 常用正则表达式：匹配HTML标记
     */
    public final static String regExp_html_1 = "/ <(.*)>.* <\\/\\1> ? <(.*) \\/>/";

    /**
     * 常用正则表达式：匹配首尾空格
     */
    public final static String regExp_startEndEmpty = "(^\\s*) ?(\\s*$)";

    /**
     * 常用正则表达式：匹配帐号是否合法(字母开头，允许5-16字节，允许字母数字下划线)
     */
    public final static String regExp_accountNumber = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";

    /**
     * 常用正则表达式：匹配国内电话号码，匹配形式如 0511-4405222 或 021-87888822
     */
    public final static String regExp_telephone = "\\d{3}-\\d{8} ?\\d{4}-\\d{7}";

    /**
     * 常用正则表达式：腾讯QQ号, 腾讯QQ号从10000开始
     */
    public final static String regExp_qq = "[1-9][0-9]{4,}";

    /**
     * 常用正则表达式：匹配中国邮政编码
     */
    public final static String regExp_postbody = "[1-9]\\d{5}(?!\\d)";

    /**
     * 常用正则表达式：匹配身份证, 中国的身份证为15位或18位
     */
    public final static String regExp_idCard = "\\d{15} ?\\d{18}";

    /**
     * 常用正则表达式：IP
     */
    public final static String regExp_ip = "\\d+\\.\\d+\\.\\d+\\.\\d+";

    /**
     * 常用正则表达式：手机号
     */
    public final static String regExp_mobile = "^0?(13[0-9]|15[012356789]|18[01236789]|14[57])[0-9]{8}$";

    /**
     * 常用正则表达式：英文和标点
     */
    public final static String regExp_enPunctuation = "^[a-zA-Z-`~!@#$%^&* ()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+$";
    /**
     * 常用正则表达式：英文和标点、数字
     */
    public final static String regExp_enPunctuation1 = "^[0-9a-zA-Z-`~!@#$%^&* ()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+$";

    /**
     * 常用正则表达式：英文、数字和中文
     */
    public final static String regExp_enNumCn = "^[A-Za-z\\d\\u4e00-\\u9fa5]+$";
    /**
     * 字符编码
     */
    public final static String encoding = "UTF-8";

    /**
     * 判断字符是否为空 或"null"
     *
     * @param s
     *            字符
     * @return true：为空，false：有值
     */
    public static boolean isEmpty(Object s) {
        if (s != null) {
            String stu = String.valueOf(s);
            return stu == null || stu.length() <= 0 || stu.equals("null") || stu.equals("[]");
        }

        return true;
    }

    /**
     * 验证字符串是否匹配指定正则表达式
     *
     * @param content
     * @param regExp
     * @return
     */
    public static boolean regExpVali(String content, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }

    /**
     * double精度调整
     *
     * @param doubleValue 需要调整的值123.454
     * @param format      目标样式".##"
     * @return
     */
    public static String decimalFormat(double doubleValue, String format) {
        DecimalFormat myFormatter = new DecimalFormat(format);
        String formatValue = myFormatter.format(doubleValue);
        return formatValue;
    }


    /**
     * 根据内容类型判断文件扩展名
     *
     * @param contentType 内容类型
     * @return
     */
    public static String getFileExt(String contentType) {
        String fileExt = "";
        if ("image/jpeg".equals(contentType)) {
            fileExt = ".jpg";

        } else if ("audio/mpeg".equals(contentType)) {
            fileExt = ".mp3";

        } else if ("audio/amr".equals(contentType)) {
            fileExt = ".amr";

        } else if ("video/mp4".equals(contentType)) {
            fileExt = ".mp4";

        } else if ("video/mpeg4".equals(contentType)) {
            fileExt = ".mp4";
        }

        return fileExt;
    }

    /**
     * 获取bean名称
     *
     * @param bean
     * @return
     */
    public static String beanName(Object bean) {
        String fullClassName = bean.getClass().getName();
        String classNameTemp = fullClassName.substring(fullClassName.lastIndexOf(".") + 1, fullClassName.length());
        return classNameTemp.substring(0, 1) + classNameTemp.substring(1);
    }

    // 文本中 @开头的标记
    public final static Pattern referer_pattern = Pattern.compile("@([^@^\\s^:]{1,})([\\s\\:\\,\\;]{0,1})");// @.+?[\\s:]


    /**
     * 首字母转小写
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     *
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }


    /**
     * 去除字符串中多余的空格
     *
     * @param s
     * @return 去除多余空格后的字符串
     */
    public static String removeExtraSpace(String s) {
        return s.replaceAll("[\\s*]+", " ").replaceAll("\r\n", "").replaceAll("−", "－");
    }

    public static boolean haveSpecialChar(String s) {
        if (s == null)
            return false;
        return !s.matches("[a-zA-Z0-9\\-]*");
    }

    public static String remove(String str, char remove) {
        if (!DataValidateUtils.isStringNotEmpty(str) || str.indexOf(remove) == -1) {
            return str;
        }
        char[] chars = str.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != remove) {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }

    public static String replaceWrap(String target) {
        return target.replaceAll("\\r+\\n+", "");
    }


    public static String padLeft(String oriStr, int len, char alexin) {
        String str = "";
        int strlen = oriStr.length();
        if (strlen < len) {
            for (int i = 0; i < len - strlen; i++) {
                str = str + alexin;
            }
        } else if (strlen > len) {
            oriStr = oriStr.substring(0, len);
        }
        str = str + oriStr;
        return str;
    }

    public static String padRight(String oriStr, int len, char alexin) {
        String str = "";
        int strlen = oriStr.length();
        if (strlen < len) {
            for (int i = 0; i < len - strlen; i++) {
                str = str + alexin;
            }
        } else if (strlen > len) {
            oriStr = oriStr.substring(0, len);
        }
        str = oriStr + str;
        return str;
    }


    /**
     * 给字符串进行 "补位" 操作
     * <p>
     * 如果调用addZeroForNum("123", 5, "0")，会返回 "00123"
     * 如果调用addZeroForNum("abc", 6, "x")，会返回 "xxxabc"
     *
     * @param str       需要补位的原始字符串
     * @param strLength 目标字符串长度
     * @param appendStr 用于补位的字符串
     * @return a
     * @author Link
     * @since 2025/9/11 14:32
     */
    public static String addZeroForNum(String str, int strLength, String appendStr) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append(appendStr).append(str);// 左补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }


    /**
     * 中文全角转半角
     *
     * @param string
     * @return
     */
    public static String full2Half(String string) {
        if (!DataValidateUtils.isStringNotEmpty(string)) {
            return string;
        }

        char[] charArray = string.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == 12288) {
                charArray[i] = ' ';
            } else if (charArray[i] >= ' ' && charArray[i] <= 65374) {
                charArray[i] = (char) (charArray[i] - 65248);
            } else {

            }
        }

        return new String(charArray);
    }

    /**
     * 中文半角转全角
     *
     * @param value
     * @return
     */
    public static String half2Full(String value) {
        if (!DataValidateUtils.isStringNotEmpty(value)) {
            return "";
        }
        char[] cha = value.toCharArray();

        /**
         * full blank space is 12288, half blank space is 32 others :full is
         * 65281-65374,and half is 33-126.
         */
        for (int i = 0; i < cha.length; i++) {
            if (cha[i] == 32) {
                cha[i] = (char) 12288;
            } else if (cha[i] < 127) {
                cha[i] = (char) (cha[i] + 65248);
            }
        }
        return new String(cha);
    }

    /**
     * 将字符串中的连续的多个换行缩减成一个换行
     *
     * @param str 要处理的内容
     * @return 返回的结果
     */
    public static String replaceLineBlanks(String str) {
        String result = "";
        if (DataValidateUtils.isStringNotEmpty(str)) {
            Pattern p = Pattern.compile("(\r?\n(\\s*\r?\n)+)");
            Matcher m = p.matcher(str);
            result = m.replaceAll("\r\n");
        }
        // 一个换行符都不留
        result = result.replaceAll("\r\n", " ");
        return result;
    }

    public static String replaceJpJiaMing(String jpTxt, boolean shiftJisFlag) {
        if (!DataValidateUtils.isStringNotEmpty(jpTxt)) {
            return "";
        }
        String text = jpTxt.replaceAll("[―‐]+", "－").replaceAll("[ｯ]+", "ッ")
//				.replaceAll("[Ⅱ]+", "　")
                .replaceAll("[･]+", "　")
//				.replaceAll("[Ⅰ]+", "　")
                .replaceAll("[ｭ]+", "ュ")
//				.replaceAll("[Ⅲ]+", "　")
                .replaceAll("[’]+", "　").replaceAll("[α]+", "　").replaceAll("[∇]+", "　")
                .replaceAll("－", "-")
                .replace("_", "-")
                .replaceAll("ｮ+", "　").replaceAll("ｬ", "ャ").replaceAll(",", "　").replaceAll("髙", "高").replaceAll("'", "　").replaceAll("\r\n", "").replaceAll("\n", "");


        try {
            if (shiftJisFlag) {
                text = text.replaceAll("[・]+", "　");
                String newText = new String(text.getBytes("Shift-JIS"), "Shift-JIS");
                return newText.replaceAll("\\?", "　");
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return text;
    }


    public static String getPinYin(String message) {

        if (!DataValidateUtils.isStringNotEmpty(message)) {
            return null;
        }
        char[] inputChar = null;
        inputChar = message.toCharArray();
        int inputCharLength = inputChar.length;
        String[] piword = new String[inputCharLength];
        HanyuPinyinOutputFormat hPinyinOutputFormat = new HanyuPinyinOutputFormat();
        // 大小写
        hPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        hPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        hPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        String piStr = "";
        try {
            for (int i = 0; i < inputCharLength; i++) {
                // 判断是否为汉字字符
                if (java.lang.Character.toString(inputChar[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    piword = PinyinHelper.toHanyuPinyinStringArray(inputChar[i], hPinyinOutputFormat);
                    piStr += piword[0];
                } else
                    piStr += java.lang.Character.toString(inputChar[i]);
            }
            // System.out.println(t4);
            return piStr;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return piStr;
    }


    /**
     * 判断字符串是否为日文
     *
     * @param input
     * @return
     */
    public static boolean isJapanese(String input) {
        try {
            return input.getBytes("shift-jis").length >= (2 * input.length());
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 驼峰转 下划线
     *
     * @param camelCaseStr 驼峰字符串
     * @return 带下滑线的String
     */
    public static String toUnderlineCase(String camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }
        // 将驼峰字符串转换成数组
        char[] charArray = camelCaseStr.toCharArray();
        StringBuffer buffer = new StringBuffer();
        //处理字符串
        for (int i = 0, l = charArray.length; i < l; i++) {
            if (charArray[i] >= 65 && charArray[i] <= 90) {
                buffer.append("_").append(charArray[i] += 32);
            } else {
                buffer.append(charArray[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param name
     * @param isClass
     * @return
     */
    public static String toLowerCaseAndUpperCaseFirstOne(String name) {
        String newName = "";
        name = name.toLowerCase();
        String[] list = name.split("_");

        for (int i = 0; i < list.length; i++) {
            String stu = list[i].substring(0, 1);
            if (i == 0) {
                newName += stu + list[i].substring(1);
            } else {
                newName += stu.toUpperCase() + list[i].substring(1);
            }
        }

        return newName;
    }

    /**
     * 全角字符串转换半角字符串
     *
     * @param fullStr
     * @return
     */
    public static String fullToHalf(String fullStr) {
        if (null == fullStr || fullStr.length() <= 0) {
            return "";
        }
        char[] charArray = fullStr.toCharArray();
        //对全角字符转换的char数组遍历
        for (int i = 0; i < charArray.length; ++i) {
            int charIntValue = (int) charArray[i];
            //如果符合转换关系,将对应下标之间减掉偏移量65248;如果是空格的话,直接做转换
            if (charIntValue >= 65281 && charIntValue <= 65374) {
                charArray[i] = (char) (charIntValue - 65248);
            } else if (charIntValue == 12288) {
                charArray[i] = (char) 32;
            }
        }
        return new String(charArray);
    }

}
