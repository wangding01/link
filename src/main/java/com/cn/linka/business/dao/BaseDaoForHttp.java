package com.cn.linka.business.dao;

import lombok.Data;

@Data
public class BaseDaoForHttp<T> {
    private Integer errorCode;
    private String errMsg;
    private T t;

    public BaseDaoForHttp(Integer errorCode, String errMsg, T t) {
        this.errorCode = errorCode;
        this.errMsg = errMsg;
        this.t = t;
    }

    public BaseDaoForHttp(Integer errorCode, String errMsg) {
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }

    public static <T> BaseDaoForHttp success(T t) {
        return new BaseDaoForHttp(200, "成功", t);
    }

    public static BaseDaoForHttp success() {
        return new BaseDaoForHttp(200, "成功");
    }

    public static BaseDaoForHttp fail() {
        return new BaseDaoForHttp(500, "系统内部异常");
    }
}
