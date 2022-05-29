package com.cn.linka.business.service;

import com.cn.linka.business.bean.LinkPageNext;
import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.UserOrderCreateRequest;
import com.cn.linka.business.dao.UserOrderCreateResponse;
import com.cn.linka.business.dao.UserOrderDao;

import java.util.List;

public interface UserOrderService {
    BaseDaoForHttp<UserOrderCreateResponse> createOrder(UserOrderCreateRequest userOrderCreateRequest);

    BaseDaoForHttp<List<UserOrderDao>> getOrder(String userId);

    BaseDaoForHttp<List<UserOrderDao>> getEffectOrder(String userId);

    BaseDaoForHttp completeOrder(String orderId,String otherId);

    BaseDaoForHttp<UserOrderDao> getOrderByOrderId(String userId, String orderId);

    BaseDaoForHttp<LinkPageNext> getOrderPage(String userId, int pageSize, long nextId);
}
