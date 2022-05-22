package com.cn.linka.business.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户对外展示信息
 */
@Data
@ApiModel(value="UserLinkBase",description="用户对外展示信息")
public class UserLinkBase {
    private String userId;
    private String userName;
    private String email;
    private String phone;
    private String userImg;
    private String userStatus;
    @ApiModelProperty("会员到期时间")
    private Date MemberUntilDate;
    @ApiModelProperty("用户主页信息")
    private UserPortalDao userPortalDao;
}
