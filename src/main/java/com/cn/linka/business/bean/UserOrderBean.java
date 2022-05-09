package com.cn.linka.business.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
public class UserOrderBean implements Serializable {
    private Long id;
    private String orderId;
    private String userId;
    private String otherId;
    private String orderStatus;
    private Long memberMenuId;
    private Date completeDt;
    private Date endDt;
    private Date createDt;
    private Date updateDt;
}
