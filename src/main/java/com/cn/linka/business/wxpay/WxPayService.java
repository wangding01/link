package com.cn.linka.business.wxpay;

import com.cn.linka.business.dao.BaseDaoForHttp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-05-07 11:08
 */
public interface WxPayService {
    String pay(WxPayDto dto) throws Exception;

    void notify(HttpServletRequest request, HttpServletResponse response) throws Exception;
}