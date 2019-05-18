package com.db.base;

/**
 *微服务接口统一返回码
 */
public class BaseResponse<T> {
    /**响应码*/
    private Integer code;
    /**响应信息*/
    private String msg;
    /**响应体*/
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseResponse(){

    }
    public BaseResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
