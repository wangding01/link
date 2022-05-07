package com.cn.linka.business.controller;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.wxpay.WxPayDto;
import com.cn.linka.business.wxpay.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xxs on 2021/7/30 10:54
 *
 * @Description 公众号微信支付
 * @Version 2.9
 */
@RestController
@RequestMapping("/wxPay")
@Api(value = "用户支付controller", tags = {"用户支付接口"})
public class WxPayController {
    @Autowired
    private WxPayService payService;


    /**
     * @Author: xxs
     * @param dto
     * @param request
     * @Date: 2021/7/30 11:55
     * @Description:  公众号微信支付
     * @Version: 2.9
     * @Return: com.jch.boot.component.Result<java.lang.String>
     */
//    @PostMapping("/pay")
//    @ApiOperation("微信支付")
//    public BaseDaoForHttp<String> pay(@RequestBody WxPayDto dto, HttpServletRequest request) throws Exception {
//        return payService.pay(dto,request);
//    }


    /**
     * @Author: xxs
     * @param request
     * @param response
     * @Date: 2021/7/30 11:55
     * @Description:  支付回调
     * @Version: 2.9
     * @Return: void
     */
    @PostMapping("/notify")
    @ApiOperation("微信支付回调")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        payService.notify(request,response);
    }

}