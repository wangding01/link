package com.cn.linka.business.mapper;


import com.cn.linka.business.bean.UserOrderBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOrderMapper {
    List<UserOrderBean> queryAllByUserId(String userId);

    Optional<UserOrderBean> queryAllByOrderId(String userId,String orderId);

    Optional<UserOrderBean> queryByOrderId(String orderId);

    void insert(UserOrderBean userOrderBean);

    int updateStatus(String orderId,String otherId);

    int updateStatusByOrderId(String otherId,String orderStatus);
}

