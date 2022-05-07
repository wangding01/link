package com.cn.linka.business.service.impl;


import com.cn.linka.business.bean.UserOrderBean;
import com.cn.linka.business.dao.*;
import com.cn.linka.business.mapper.MemberMenuMapper;
import com.cn.linka.business.mapper.UserOrderMapper;
import com.cn.linka.business.service.MemberMenuService;
import com.cn.linka.business.service.UserOrderService;
import com.cn.linka.business.wxpay.WxPayDto;
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
    private final static String ORDER_INIT_STATUS = "1";
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
        try {
            wxPayService.pay(dto);
        }catch (Exception e){
            log.error("微信支付拉起失败：{}",e.getMessage());
        }

        return BaseDaoForHttp.success(UserOrderCreateResponse.builder().orderId(orderId).build());
    }

    @Override
    public BaseDaoForHttp<UserOrderDao> getOrder(String userId) {
        Optional<List<UserOrderBean>> userOrderBeans = userOrderMapper.queryAllByUserId(userId);
        if (!userOrderBeans.isPresent()) {
            throw new BusException(BusinessExceptionEnum.THE_USER_NO_ORDER);
        }
        return BaseDaoForHttp.success(userOrderBeans.get());
    }

    @Override
    public BaseDaoForHttp completeOrder(String orderId) {
        if (userOrderMapper.updateStatus(orderId) < 1) {
            throw new BusException(BusinessExceptionEnum.ORDER_ERROR);
        }
        return BaseDaoForHttp.success();
    }
}
