package com.cn.linka.business.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDaoForHttpByPageNo<T> implements Serializable {
    private Integer errorCode;
    private String errMsg;
    private int offset;
    private int pageSize;
    private int total;
    private T Body;

    public BaseDaoForHttpByPageNo(Integer errorCode, String errMsg) {
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }

    public BaseDaoForHttpByPageNo(Integer errorCode, String errMsg, T t, int offset, int pageSize, int total) {
        this.errorCode = errorCode;
        this.errMsg = errMsg;
        this.Body = t;
        this.offset = offset;
        this.pageSize = pageSize;
        this.total = total;
    }

    public static <T> BaseDaoForHttpByPageNo success(T Body, int offset, int total, int pageSize) {
        return new BaseDaoForHttpByPageNo(200, "成功", Body, offset, pageSize, total);
    }

}
