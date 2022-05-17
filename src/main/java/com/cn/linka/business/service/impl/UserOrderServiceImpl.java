package com.cn.linka.business.service.impl;


import com.cn.linka.business.bean.UserOrderBean;
import com.cn.linka.business.dao.*;
import com.cn.linka.business.mapper.MemberMenuMapper;
import com.cn.linka.business.mapper.UserOrderMapper;
import com.cn.linka.business.service.MemberMenuService;
import com.cn.linka.business.service.UserOrderService;
import com.cn.linka.business.wxpay.WxPayDto;
import com.cn.linka.business.wxpay.WxPayQueryBean;
import com.cn.linka.business.wxpay.WxPayService;
import com.cn.linka.common.config.SnowFlake;
import com.cn.linka.common.exception.BusException;
import com.cn.linka.common.exception.BusinessExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserOrderServiceImpl implements UserOrderService {
    private final static String ORDER_INIT_STATUS = "0";
    private final static String ORDER_SUCCESS_STATUS = "1";
    private final static String WX_ORDER_STATUS_SUCCESS = "SUCCESS";//成功
    private final static String WX_ORDER_STATUS_NOTPAY = "NOTPAY";//未支付
    private final static String WX_ORDER_STATUS_CLOSED = "CLOSED";//已关闭
    private final static String ORDER_CLOSED_STATUS = "2";
    private final static String WX_ORDER_STATUS_USERPAYING = "USERPAYING";//用户支付中
    private final static String WX_ORDER_STATUS_PAYERROR = "PAYERROR";//支付失败(其他原因，如银行返回失败)
    private final static String ORDER_PAYERROR_STATUS = "3";
    private final static String WX_ORDER_STATUS_ACCEPT = "ACCEPT";//已接收，等待扣款
    @Resource
    private UserOrderMapper userOrderMapper;
    @Resource
    private MemberMenuMapper memberMenuMapper;
    @Resource
    private WxPayService wxPayService;

    @Override
    public BaseDaoForHttp<UserOrderCreateResponse> createOrder(UserOrderCreateRequest userOrderCreateRequest) {
        Optional<MemberMenuDao> menuById = memberMenuMapper.getMenuById(Long.valueOf(userOrderCreateRequest.getMenuId()));
        if (!menuById.isPresent()) {
            throw new BusException(BusinessExceptionEnum.MENU_NOT_EXIST);
        }
        String orderId = SnowFlake.nextIdString();
        UserOrderBean build = UserOrderBean.builder().memberMenuId(userOrderCreateRequest.getMenuId())
                .createDt(new Date())
                .orderId(orderId)
                .userId(userOrderCreateRequest.getUserId())
                .orderStatus(ORDER_INIT_STATUS)
                .build();
        userOrderMapper.insert(build);
        WxPayDto dto = WxPayDto.builder()
                .totalFee(menuById.get().getRealPrice())
                .outTradeNo(orderId)
                .realIp(userOrderCreateRequest.getRealIp())
                .body(menuById.get().getMemberMenuName())
                .build();
        String backUrl = "";
        try {
            backUrl = wxPayService.nativePay(dto);
        } catch (Exception e) {
            log.error("微信支付拉起失败：{}", e.getMessage());
        }
        return BaseDaoForHttp.success(UserOrderCreateResponse.builder()
                .wxUrl(backUrl)
                .orderId(orderId).build());
    }

    @Override
    public BaseDaoForHttp<List<UserOrderDao>> getOrder(String userId) {
        List<UserOrderBean> userOrderBeans = userOrderMapper.queryAllByUserId(userId);
        if (userOrderBeans != null) {
            throw new BusException(BusinessExceptionEnum.THE_USER_NO_ORDER);
        }
        return BaseDaoForHttp.success(userOrderBeans);
    }

    @Override
    public BaseDaoForHttp completeOrder(String orderId, String otherId) {
        //时间计算
        if (userOrderMapper.updateStatus(orderId, otherId) < 1) {
            throw new BusException(BusinessExceptionEnum.ORDER_ERROR);
        }
        return BaseDaoForHttp.success();
    }

    @Override
    public BaseDaoForHttp<UserOrderDao> getOrderByOrderId(String userId, String orderId) {
        Optional<UserOrderBean> userOrderBean = userOrderMapper.queryAllByOrderId(userId, orderId);
        if (!userOrderBean.isPresent()) {
            throw new BusException(BusinessExceptionEnum.ORDER_ID_IS_NULL);
        }
        if (ORDER_SUCCESS_STATUS.equals(userOrderBean.get().getOrderStatus())) {
            return BaseDaoForHttp.success(userOrderBean.get());
        }
        if (ORDER_INIT_STATUS.equals(userOrderBean.get().getOrderStatus())) {
            log.info("订单状态未修改，再次查询微信");
            WxPayQueryBean wxPayQueryBean = wxPayService.checkWxPayOrder(orderId);
            if (WX_ORDER_STATUS_SUCCESS.equals(wxPayQueryBean.getTradeState())) {//成功则修改订单状态成功
                completeOrder(orderId,wxPayQueryBean.getOpenId());
            }
            if (WX_ORDER_STATUS_CLOSED.equals(wxPayQueryBean.getTradeState())) {//订单关闭
                userOrderMapper.updateStatusByOrderId(orderId,ORDER_CLOSED_STATUS);
            }
            if (WX_ORDER_STATUS_PAYERROR.equals(wxPayQueryBean.getTradeState())) {//订单关闭
                userOrderMapper.updateStatusByOrderId(orderId,ORDER_PAYERROR_STATUS);
            }
        }
        return BaseDaoForHttp.success(userOrderMapper.queryAllByOrderId(userId, orderId).get());
    }
}
