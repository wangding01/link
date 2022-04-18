package com.cn.linka.business.mapper;


import com.cn.linka.business.bean.UserOrderBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOrderMapper {
    Optional<List<UserOrderBean>> queryAllByUserId(String userId);

    void insert(UserOrderBean userOrderBean);

    int updateStatus(String orderId);
}

