package com.cn.linka.common.exception;

import lombok.Data;

@Data
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

    public BusException(BusinessExceptionEnum businessExceptionEnum) {
        this.errorMsg = businessExceptionEnum.getName();
        this.errorCode = businessExceptionEnum.getCode();
    }
}
