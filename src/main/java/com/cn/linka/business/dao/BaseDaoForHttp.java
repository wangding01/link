package com.cn.linka.business.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDaoForHttp<T> implements Serializable {
    private Integer errorCode;
    private String errMsg;
    private T Body;

    public BaseDaoForHttp(Integer errorCode, String errMsg, T t) {
        this.errorCode = errorCode;
        this.errMsg = errMsg;
        this.Body = t;
    }

    public BaseDaoForHttp(Integer errorCode, String errMsg) {
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }

    public static <T> BaseDaoForHttp success(T Body) {
        return new BaseDaoForHttp(200, "成功", Body);
    }

    public static BaseDaoForHttp success() {
        return new BaseDaoForHttp(200, "成功");
    }

    public static BaseDaoForHttp fail() {
        return new BaseDaoForHttp(500, "系统内部异常");
    }
    public static BaseDaoForHttp fail(Integer errorCode, String errMsg) {
        return new BaseDaoForHttp(errorCode, errMsg);
    }
}
