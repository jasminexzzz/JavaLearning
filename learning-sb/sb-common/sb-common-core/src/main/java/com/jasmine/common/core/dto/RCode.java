package com.jasmine.common.core.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * httpCode : httpCode返回码
 * code     : 系统自定义返回码
 * msg      : 异常信息
 * @author : jasmineXz
 */
@SuppressWarnings("ALL")
public enum RCode {

    /* 成功状态码 */
    SUCCESS                (200, 20000, "成功"),
    BAD_REQUEST            (400, 40000, "错误的请求"),
    /* ──────────────────────────────────────────── 请求鉴权服务错误 ────────────────────────────────────────────────*/
    AUTH_SERVER_FAULT_UP   (400, 40001, "用户名或密码错误"),
    AUTH_SERVER_FAULT      (400, 49999, "授权处理错误"),
    AUTH_UNAUTHORIZED      (401, 41001, "权限不足"),
    AUTH_NONE_TOKEN        (401, 41002, "未授权的请求,请登陆"),
    AUTH_INVALID_TOKEN     (401, 41003, "登陆过期,请重新登录"),
    AUTH_FORBIDDEN         (403, 43001, "你没有访问权限,因此被服务器拒绝"),
    /* ──────────────────────────────────────────── 404 ────────────────────────────────────────────────────────────*/
    NOT_FOUND              (404, 40004, "找不到您请求的网页或路径"),
    /* ──────────────────────────────────────────── 服务器错误：50001-599999 ────────────────────────────────────────*/
    INTERNAL_SERVER_ERROR  (500, 50000, "服务器处理错误"),

    SERVER_UNAVAILABLE     (503, 50301, "访问过于频繁,请稍后再试"),
    ;

    private int httpCode;
    private int code;
    private String msg;

    RCode(Integer httpCode , Integer code, String message) {
        this.httpCode = httpCode;
        this.code = code;
        this.msg = message;
    }

    public Integer httpCode() {
        return this.httpCode;
    }
    public Integer code() {
        return this.code;
    }
    public String msg() {
        return this.msg;
    }


    /**
     * 根据code返回信息
     * @param code
     * @return
     */
    public static RCode byCode(int code) {
        for (RCode item : RCode.values()) {
            if (item.code == code || item.httpCode == code) {
                return item;
            }
        }
        return RCode.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String toString() {
        return this.name();
    }

    //校验重复的code值
    public static void main(String[] args) {
        RCode[] apiResultCodes = RCode.values();
        List<Integer> codeList = new ArrayList<Integer>();
        for (RCode apiResultCode : apiResultCodes) {
            if (codeList.contains(apiResultCode.code)) {
                System.out.println(apiResultCode.code);
            } else {
                codeList.add(apiResultCode.code());
            }
        }
    }
}
