package com.cn.linka.business.controller;

import com.cn.linka.business.bean.LinkPageNext;
import com.cn.linka.business.dao.*;
import com.cn.linka.business.service.MemberMenuService;
import com.cn.linka.business.service.UserOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    @Resource
    private MemberMenuService memberMenuService;

    @GetMapping(value = "/get-menus-to-user")
    @ApiOperation("用户查询会员菜单信息")
    public BaseDaoForHttp<List<MemberMenuDao>> getAllToUser() {
        return memberMenuService.getAllToUser();
    }

    @PostMapping(value = "/create-order")
    @ApiOperation("创建订单")
    public BaseDaoForHttp<UserOrderCreateResponse> createOrder(@RequestBody UserOrderCreateRequest userOrderCreateRequest,HttpServletRequest request) {
        String remoteHost = getRemoteHost(request);
        userOrderCreateRequest.setRealIp(remoteHost);
        return userOrderService.createOrder(userOrderCreateRequest);
    }

    @GetMapping(value = "/get-order-by-userId")
    @ApiOperation("查询订单")
    public BaseDaoForHttp<List<UserOrderDao>> getOrder(String userId) {
        return userOrderService.getOrder(userId);
    }

    @GetMapping(value = "/get-order-by-userId-page")
    @ApiOperation("查询订单-下拉分页")
    public BaseDaoForHttp<LinkPageNext> getOrderPageNext(String userId,int pageSize,long nextId) {
        return userOrderService.getOrderPage(userId,pageSize,nextId);
    }

    @GetMapping(value = "/get-order-by-userId-page-no")
    @ApiOperation("查询订单-带页分页")
    public BaseDaoForHttpByPageNo<List<UserOrderDao>> getOrderPageNo(String userId, int pageSize, int offset) {
        return userOrderService.getOrderPageNo(userId,pageSize,offset);
    }

    @GetMapping(value = "/get-order-by-orderId")
    @ApiOperation("查询订单-orderId")
    public BaseDaoForHttp<UserOrderDao> getOrderByOrderId(String userId, String orderId) {
        return userOrderService.getOrderByOrderId(userId, orderId);
    }
    public String getRemoteHost(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("X-Real-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }

}