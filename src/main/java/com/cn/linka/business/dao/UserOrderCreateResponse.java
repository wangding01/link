package com.cn.linka.business.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Builder
@Data
@ApiModel(value = "UserOrderCreateResponse", description = "创建订单返回体")
public class UserOrderCreateResponse implements Serializable {
    @ApiModelProperty("用户id")
    private String orderId;
}
