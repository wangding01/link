package com.cn.linka.business.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-04-14 16:45
 */
@Data
@ApiModel(value="ThemeDao",description="主题管理")
public class ThemeDao {
    @ApiModelProperty("主键-更新时必传")
    private Long id;
    @ApiModelProperty("主题名称")
    private String themeName;
    @ApiModelProperty("主题类型1免费2收费")
    private String themeType;
    @ApiModelProperty("主题状态1正常")
    private String themeStatus;
    @ApiModelProperty("主题url")
    private String themeUrl;
    @ApiModelProperty("主题缩略图url")
    private String themeThumbnailUrl;
    private Date createDt;
}