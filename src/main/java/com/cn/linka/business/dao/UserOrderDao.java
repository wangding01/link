package com.cn.linka.business.dao;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
public class UserOrderDao implements Serializable {
    private Long id;
    private String orderId;
    private String userId;
    private String orderStatus;
    private String memberMenuId;
    private Date completeDt;
    private Date endDt;
    private Date createDt;
    private Date updateDt;


}
