package com.cn.linka.common.exception;

import org.springframework.http.HttpStatus;

public class BusException extends RuntimeException {

    private Integer errorCode;
    private String errorMsg;

    public BusException(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    public BusException(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
