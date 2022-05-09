package com.cn.linka.business.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "UserOrderCreateRequest", description = "创建订单请求体")
public class UserOrderCreateRequest implements Serializable {
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("菜单id")
    private Long menuId;
    @ApiModelProperty("用户真实ip")
    private String realIp;
}
