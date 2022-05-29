package com.cn.linka.business.service.impl;


import com.cn.linka.business.bean.LinkPageNext;
import com.cn.linka.business.bean.UserOrderBean;
import com.cn.linka.business.dao.*;
import com.cn.linka.business.mapper.MemberMenuMapper;
import com.cn.linka.business.mapper.UserOrderMapper;
import com.cn.linka.business.service.UserOrderService;
import com.cn.linka.business.wxpay.WxPayDto;
import com.cn.linka.business.wxpay.WxPayQueryBean;
import com.cn.linka.business.wxpay.WxPayService;
import com.cn.linka.common.config.SnowFlake;
import com.cn.linka.common.exception.BusException;
import com.cn.linka.common.exception.BusinessExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
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
    private final static String WX_ORDER_STATUS_CLOSED = "CLOSED";//已关闭
    private final static String ORDER_CLOSED_STATUS = "2";
    private final static String WX_ORDER_STATUS_PAYERROR = "PAYERROR";//支付失败(其他原因，如银行返回失败)
    private final static String ORDER_PAYERROR_STATUS = "3";
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
        if (userOrderBeans == null || userOrderBeans.size() < 1) {
            throw new BusException(BusinessExceptionEnum.THE_USER_NO_ORDER);
        }
        return BaseDaoForHttp.success(userOrderBeans);
    }

    @Override
    public BaseDaoForHttp<List<UserOrderDao>> getEffectOrder(String userId) {
        List<UserOrderBean> userOrderDaoList = userOrderMapper.getEffectOrder(userId);
        return BaseDaoForHttp.success(userOrderDaoList);
    }

    @Override
    public BaseDaoForHttp completeOrder(String orderId, String otherId) {
        Optional<UserOrderBean> userOrderBean = userOrderMapper.queryByOrderId(orderId);
        List<UserOrderBean> effectOrder = userOrderMapper.getEffectOrder(userOrderBean.get().getUserId());
        Date startDate = new Date();
        if (effectOrder.size() > 0) {
            startDate = effectOrder.get(0).getEndDt();
        }
        Optional<MemberMenuDao> menuById = memberMenuMapper.getMenuById(userOrderMapper.queryByOrderId(orderId).get().getMemberMenuId());
        Date endDt = DateUtils.addDays(startDate, menuById.get().getMenuTime());
        //时间计算
        log.info("开始修改订单信息订单号：{},openId:{},开始计算时间点为：{},有效期为：{}", orderId, otherId, startDate.toString(), menuById.get().getMenuTime());
        if (userOrderMapper.updateStatus(orderId, otherId, endDt) < 1) {
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
                completeOrder(orderId, wxPayQueryBean.getOpenId());
            }
            if (WX_ORDER_STATUS_CLOSED.equals(wxPayQueryBean.getTradeState())) {//订单关闭
                userOrderMapper.updateStatusByOrderId(orderId, ORDER_CLOSED_STATUS);
            }
            if (WX_ORDER_STATUS_PAYERROR.equals(wxPayQueryBean.getTradeState())) {//订单关闭
                userOrderMapper.updateStatusByOrderId(orderId, ORDER_PAYERROR_STATUS);
            }
        }
        return BaseDaoForHttp.success(userOrderMapper.queryAllByOrderId(userId, orderId).get());
    }

    @Override
    public BaseDaoForHttp<LinkPageNext> getOrderPage(String userId, int pageSize, long nextId) {
        List<UserOrderBean> list = userOrderMapper.getOrderPage(userId, pageSize, nextId);
        if (list == null || list.size() < 1) {
            throw new BusException(BusinessExceptionEnum.THE_USER_NO_ORDER);
        }
        Boolean isLast = false;
        if (list.size() < pageSize) {
            isLast = true;
        }
        Long maxId = list.get(0).getId();
        return BaseDaoForHttp.success(LinkPageNext.createBasePage(maxId,isLast,pageSize,list));
    }

}
