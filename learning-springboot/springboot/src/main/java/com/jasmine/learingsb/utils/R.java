package com.jasmine.learingsb.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author : jasmineXz
 */
public class R implements Serializable{
    private Integer code;           /** 系统内部自定义的返回值编码  {@link SysResponseCode} */
    private String  msg;            // 返回信息
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String  from;           // 来源服务
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object  data;           // 响应体

    private R(){}

    /* ========================================================================================= */
    /* =                                      返回成功                                         = */
    /* ========================================================================================= */

    /** 返回成功 @param data 对象类型的data数据 */
    public static R ok(){
        return R.initR(SysResponseCode.SUCCESS.code(), SysResponseCode.SUCCESS.msg(), null, null);
    }

    /** 返回成功 @param data 对象类型的data数据 */
    public static R ok(Object data){
        return R.initR(SysResponseCode.SUCCESS.code(), SysResponseCode.SUCCESS.msg(), null, data);
    }

    /** 熔断等操作添加from来查看原因 */
    public static R ok(Object data,String from){
        return R.initR(SysResponseCode.SUCCESS.code(), SysResponseCode.SUCCESS.msg(), from, data);
    }

    /** 返回成功 */
    public static R ok(JsonNode jsonNode){
        return R.initR(SysResponseCode.SUCCESS.code(), SysResponseCode.SUCCESS.msg(), null, JsonUtil.json2Object(jsonNode.toString(),Object.class ));
    }

    /* ========================================================================================= */
    /* =                                      返回失败                                         = */
    /* ========================================================================================= */

    /** 50000 默认返回信息 */
    public static R fault(){
        return R.initR(SysResponseCode.INTERNAL_SERVER_ERROR.code(), SysResponseCode.INTERNAL_SERVER_ERROR.msg(), null,null );
    }

    /** 50000/自定义返回信息 */
    public static R fault(String msg){
        return R.initR(SysResponseCode.INTERNAL_SERVER_ERROR.code(), msg, null,null );
    }

    /** 自定义返回值/自定义返回信息 */
    public static R fault(int code, String msg){
        return R.initR(code, msg != null ? clearStr(msg) : SysResponseCode.byCode(code).msg(), null, null);
    }

    /** 自定义返回值/自定义返回信息 */
    public static R fault(int code, String msg,String from){
        return R.initR(code, msg != null ? clearStr(msg) : SysResponseCode.byCode(code).msg(), from, null);
    }



    /** 初始化异常返回对象 */
    private static R initR(int code,String msg,String from,Object data){
        R r = new R();
        r.setCode(code);
        r.setMsg(msg);
        r.setFrom(from);
        r.setData(data);
        return r;
    }

    /**
     * 对返回字符串进行一些格式处理
     * - 例如:
     *   - gateway返回的错误信息都包含双引号,序列化后会有多引号的情况 : ""内容""
     * @param str 字符串对象
     * @return 处理后的对象
     */
    private static String clearStr(String str){
        return StringUtils.replace(str, "\"", "");
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
