package com.cn.linka.business.service;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.UserOrderCreateRequest;
import com.cn.linka.business.dao.UserOrderCreateResponse;
import com.cn.linka.business.dao.UserOrderDao;

public interface UserOrderService {
    BaseDaoForHttp<UserOrderCreateResponse> createOrder(UserOrderCreateRequest userOrderCreateRequest);

    BaseDaoForHttp<UserOrderDao> getOrder(String userId);

    BaseDaoForHttp completeOrder(String orderId);
}
