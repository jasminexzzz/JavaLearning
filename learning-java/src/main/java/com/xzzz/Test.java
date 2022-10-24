package com.xzzz;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    private Pattern pattern;

    // 定义正则表达式，域名的根需要自定义，这里不全
    private static final String RE_TOP = "[\\w-]+\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co)\\b()*";

    // 构造函数
    public Test() {
        pattern = Pattern.compile(RE_TOP , Pattern.CASE_INSENSITIVE);
    }


    public String getTopDomain(String url) {
        String result = url;
        try {
            Matcher matcher = this.pattern.matcher(url);
            matcher.find();
            result = matcher.group();
        } catch (Exception e) {
            System.out.println("[getTopDomain ERROR]====>");
            e.printStackTrace();
        }
        return result;
    }


    public static void main(String[] args) {
        Test obj = new Test();

        // 示例
        String url = "www.baidu.cc";
        String res1 = obj.getTopDomain(url);
        System.out.println(url + " ==> " + res1);

        url = "ac.asd.c.sina.com.cn";
        String res2 = obj.getTopDomain(url);
        System.out.println(url + " ==> " + res2);

        url = "whois.chinaz.com/reverse?ddlSearchMode=1";
        String res3 = obj.getTopDomain(url);
        System.out.println(url + " ==> " + res3);

        url = "http://write.blog.csdn.net/";
        String res4 = obj.getTopDomain(url);
        System.out.println(url + " ==> " + res4);

        url = "http://write.test.org.nz/";
        String res5 = obj.getTopDomain(url);
        System.out.println(url + " ==> " + res5);
    }

}
