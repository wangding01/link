package com.cn.linka.common.exception;

import com.cn.linka.business.dao.BaseDaoForHttp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-04-14 10:28
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理自定义异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BusException.class)
    public BaseDaoForHttp handleBusException(BusException ex) {
        BaseDaoForHttp result = BaseDaoForHttp.fail();
        if (StringUtils.isNotBlank(ex.getErrorMsg())) {
            result.setErrMsg(ex.getErrorMsg());
        }
        if (StringUtils.isNotBlank(ex.getErrorCode().toString())) {
            result.setErrorCode(ex.getErrorCode());
        }
        return result;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public BaseDaoForHttp handleBusException(ConstraintViolationException ex) {
        BaseDaoForHttp result = BaseDaoForHttp.fail();
        if (StringUtils.isNotBlank(ex.getMessage())) {
            result.setErrMsg(ex.getMessage());
        }
        result.setErrorCode(500);
        return result;
    }

    /**
     * 其他异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public BaseDaoForHttp handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return BaseDaoForHttp.fail();
    }
}