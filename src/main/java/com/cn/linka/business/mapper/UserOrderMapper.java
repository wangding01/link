package com.cn.linka.business.mapper;


import com.cn.linka.business.bean.UserOrderBean;
import com.cn.linka.business.dao.UserOrderDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserOrderMapper {
    List<UserOrderBean> queryAllByUserId(String userId);

    Optional<UserOrderBean> queryAllByOrderId(String userId,String orderId);

    Optional<UserOrderBean> queryByOrderId(String orderId);

    void insert(UserOrderBean userOrderBean);

    int updateStatus(String orderId, String otherId, Date endDt);

    int updateStatusByOrderId(String otherId,String orderStatus);

    List<UserOrderBean> getEffectOrder(String userId);

    int syncOtherId(String order_no, String otherId);

    List<UserOrderBean> getOrderPage(String userId, int pageSize, long nextId);

    int getTotalSizeByUserId(String userId);

    List<UserOrderDao> getOrderPageNo(String userId, int startNo, int pageSize);
}

