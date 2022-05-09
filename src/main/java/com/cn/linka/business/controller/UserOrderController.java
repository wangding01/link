package com.cn.linka.business.controller;

import com.cn.linka.business.dao.*;
import com.cn.linka.business.service.FileService;
import com.cn.linka.business.service.UserOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: linka
 * @description: 文件处理
 * @author: wangding
 * @create: 2022-04-11 16:09
 */
@RestController
@Api(value = "用户订单controller", tags = {"用户订单接口"})
public class UserOrderController {
    @Resource
    private UserOrderService userOrderService;

    @PostMapping(value = "/create-order")
    @ApiOperation("创建订单")
    public BaseDaoForHttp<UserOrderCreateResponse> createOrder(@RequestBody UserOrderCreateRequest userOrderCreateRequest) {
        return userOrderService.createOrder(userOrderCreateRequest);
    }

    @GetMapping(value = "/get-order-by-userId")
    @ApiOperation("查询订单")
    public BaseDaoForHttp<List<UserOrderDao>> getOrder(String userId) {
        return userOrderService.getOrder(userId);
    }

    @GetMapping(value = "/get-order-by-orderId")
    @ApiOperation("查询订单-orderId")
    public BaseDaoForHttp<UserOrderDao> getOrderByOrderId(String userId, String orderId) {
        return userOrderService.getOrderByOrderId(userId, orderId);
    }
}