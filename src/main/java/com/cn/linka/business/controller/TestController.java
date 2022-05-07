package com.cn.linka.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-05-07 11:40
 */

@Controller
public class TestController {
    @RequestMapping("link-test/wxPayTest")
    public String test(HttpServletResponse response)  {
        return "wxPay1";
    }
}