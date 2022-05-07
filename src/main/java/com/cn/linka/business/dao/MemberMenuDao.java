package com.cn.linka.business.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-04-14 17:08
 */
@Data
@ApiModel(value="MemberMenuDao",description="会员菜单")
public class MemberMenuDao implements Serializable {
    @ApiModelProperty("主键-更新时必传")
    private Long id;
    @ApiModelProperty("菜单名称")
    private String memberMenuName;
    @ApiModelProperty("此菜单有效期（天）")
    private Integer menuTime;
    @ApiModelProperty("折扣前价格")
    private Integer showPrice;
    @ApiModelProperty("折扣后价格-真实售价")
    private Integer realPrice;
    @ApiModelProperty("菜单状态：1正常在售")
    private String menuStatus;
    private Date createDt;

}