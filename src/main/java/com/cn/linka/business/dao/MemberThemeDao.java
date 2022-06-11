package com.cn.linka.business.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="MemberThemeDao",description="用户会员关联的主题")
public class MemberThemeDao implements Serializable {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("会员菜单类型")
    private Integer memberType;
    @ApiModelProperty("会员菜单关联的主题id")
    private String themeIds;
    @ApiModelProperty("会员菜单关联的主题状态")
    private Integer themeStatus;
}
