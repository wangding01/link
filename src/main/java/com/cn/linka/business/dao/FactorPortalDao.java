package com.cn.linka.business.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-04-12 14:26
 */
@Data
@ApiModel(value="FactorPortalDao",description="用户主页详情链接信息")
public class FactorPortalDao {
    @ApiModelProperty("图标url")
    private String imgUrL;
    @ApiModelProperty("链接title")
    private String linkTitle;
    @ApiModelProperty("链接地址")
    private String indexUrl;
    @ApiModelProperty("是否激活 1正常")
    private String openStatus;
    @ApiModelProperty("排序")
    private Integer order;
}