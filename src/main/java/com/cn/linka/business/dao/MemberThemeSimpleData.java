package com.cn.linka.business.dao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberThemeSimpleData {
    @ApiModelProperty("主题名称")
    private String themeName;
    @ApiModelProperty("主题url")
    private String themeUrl;
    @ApiModelProperty("主题缩略图url")
    private String themeThumbnailUrl;
    @ApiModelProperty("主题详情")
    private String themeData;
}
