package com.jasmine.A1_java.high.工具类.字符串工具;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by flame on 2018/8/27下午2:35.
 * 字符串工具类
 */
public class StringUtil extends StringUtils {

    private static final String UNDEFINED = "undefined";
    private static final String NULL = "null";
    private static final String PWD = "MESSAGE_FLAG";
    private static final String LIKE = "%s%";

    /**
     * 检查字符串长度是否超过指定限制
     *
     * @param str
     * @param size
     * @return false : 长度不符合  true : 长度符合
     */
    public static Boolean checkStringLength(String str, Integer size) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        if (str.length() > size) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串不为空并且不为undefined
     *
     * @param params
     * @return
     */
    public static boolean checkBlank(String params) {
        if (StringUtils.isBlank(params))
            return true;
        if (UNDEFINED.equals(params))
            return true;
        if (NULL.equals(params))
            return true;
        return false;
    }

    public static boolean isAllBlank(CharSequence... css) {
        for (CharSequence charSequence : css) {
            if (isNotBlank(charSequence)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串不为空并且不为undefined
     *
     * @param params
     * @return
     */
    public static boolean checkNotBlank(String params) {
        if (StringUtils.isBlank(params))
            return false;
        if (UNDEFINED.equals(params))
            return false;
        if (NULL.equals(params))
            return false;
        return true;
    }


    public static String getMessageFlag(Long from, Long to) {
        if (from == null || to == null)
            return null;
        if (from.longValue() >= to.longValue()) {
            return DigestUtils.md5Hex(to.longValue() + PWD + from.longValue());
        } else {
            return DigestUtils.md5Hex(from.longValue() + PWD + to.longValue());
        }

    }

    /**
     * 获取隐藏的手机号码(中间4位隐藏)
     *
     * @param mobile
     * @return
     */
    public static String getHideMobile(String mobile) {
        if (StringUtil.isBlank(mobile) || mobile.length() < 11) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4, mobile.length());
    }

    /**
     * 获取隐藏的手机号码(中间7位隐藏)
     *
     * @param mobile
     * @return
     */
    public static String getHideMobileSeven(String mobile) {
        if (StringUtil.isBlank(mobile) || mobile.length() < 11) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{7}(\\d)", "$1*******$2");
    }

    /**
     * 获取隐藏的手机号码(后4位隐藏)
     *
     * @param mobile
     * @return
     */
    public static String getHideMobile2(String mobile) {
        if (StringUtil.isBlank(mobile) || mobile.length() < 11) {
            return mobile;
        }
        return "*******" + mobile.substring(7, mobile.length());
    }

    /**
     * 隐藏-部分字段
     *
     * @param str
     * @return
     */
    public static String hideString(String str) {
        if (StringUtil.isBlank(str) || str.length() == 1) {
            return str;
        }
        final int strLen = str.length();
        if (strLen == 2) {
            String newStr = str.substring(0, 1) + "*";
            return newStr;
        }
        // 隐藏中间所有部分
        String newStr = str.substring(0, 1) + "*" + str.substring(str.length() - 1, str.length());
        return newStr;
    }

    /**
     * 隐藏-尾部所有字
     *
     * @param str
     * @return
     */
    public static String hideAllString(String str) {
        if (StringUtil.isBlank(str) || str.length() == 1) {
            return str;
        }
        final int strLen = str.length();
        if (strLen == 2) {
            String newStr = str.substring(0, 1) + "*";
            return newStr;
        }
        // 隐藏中间所有部分
        String newStr = str.substring(0, 1) + "**";
        return newStr;
    }


    /**
     * String转变为List
     *
     * @param str
     * @param regex
     * @return
     */
    public static List<String> toList(String str, String regex) {
        if (StringUtil.isBlank(str)) {
            return null;
        }
        String[] variables = str.split(regex);
        return Arrays.asList(variables);
    }

//    /**
//     * 短信--内容和变量组合
//     *
//     * @param content
//     * @param variable
//     * @return
//     */
//    public static String contentVariable(String content, String variable) {
//        if (StringUtil.isNotBlank(variable)) {
//            // 将variable转变为
//            List<JSONObject> list = JsonUtil.parseArrayJson(variable, JSONObject.class);
//            int i = 0;
//            for (JSONObject jsonObject : list) {
//                i++;
//                String var = (String) jsonObject.values().iterator().next();
//                String target = "{" + i + "}";
//                content = content.replace(target, var);
//            }
//        }
//        return content;
//    }
//
//    /**
//     * 获取--JsonArrayString的值
//     *
//     * @param jsonArrayString
//     * @return
//     */
//    public static String jsonArrayStringToValues(String jsonArrayString) {
//        if(StringUtil.isBlank(jsonArrayString)) {
//            return "";
//        }
//        List<JSONObject> list = JsonUtil.parseArrayJson(jsonArrayString, JSONObject.class);
//        String values = "";
//        for (JSONObject jsonObject : list) {
//            String var = (String) jsonObject.values().iterator().next();
//            if(StringUtil.isNotBlank(var)) {
//                values += String.format("%s%s",var,",");
//            }
//        }
//        return values.substring(0,values.length()-1); // 去掉最后一个逗号
//    }

    public static String getLikeParams(String productTitle) {
        return LIKE.replace("s", productTitle);
    }

    /**
     * 将字符串{1},{2}....{n}统一替换为{$var}
     *
     * @return
     */
    public static String replaceNumToVar(String content) {
        int i = 0;
        while (true) {
            i++;
            String target = "{" + i + "}";
            if (!content.contains(target)) {
                break;
            }
            content = content.replace(target, "{$var}");
        }
        return content;
    }

    // 校验是否符合手机号码规则
    public static boolean isMobile(String mobile){
        if(isBlank(mobile) || mobile.length() != 11){
            return false;
        }
        return mobile.matches("^1[3|4|5|7|8|9|6][0-9]\\d{8}$");
    }

    /**
     * 如果info为空，就返回“dispDefault”
     *
     * @param info
     * @param dispDefault
     * @return
     */
    public static String getDispInfo(final String info, final String dispDefault) {
        if(info == null || info.trim().isEmpty()){
            return dispDefault;
        }
        return info;
    }

    /**
     * 判断一个字符串中中文个数
     * @param value
     * @return
     */
    public static Integer getChineseNum(String value) {
        int num = 0;
        if (null == value) {
            return num;
        }
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                num ++;
            }
        }
        return num;
    }

    /**
     * 获取指定长度空字符串
     * @param length
     * @return
     */
    public static String getSpaceChar(Integer length) {
        String res = "";
        for (int i = 0; i < length; i++) {
            res += " ";
        }
        return  res;
    }
}
