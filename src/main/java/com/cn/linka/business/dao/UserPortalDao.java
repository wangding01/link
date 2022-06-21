package com.cn.linka.business.dao;

import com.alibaba.fastjson.JSON;
import com.cn.linka.business.bean.UserPortalBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @program: linka
 * @description: 用户主页信息
 * @author: wangding
 * @create: 2022-04-11 14:41
 */
@Data
@ApiModel(value="UserPortalDao",description="用户主页信息")
public class UserPortalDao implements Serializable {
    @ApiModelProperty("用户id-更新时必传")
    private String userId;
    @ApiModelProperty("主页头像")
    private String portalHeadUrl;
    @ApiModelProperty("简介")
    private String introduction;
    @ApiModelProperty("详解介绍")
    private String userDetail;
    @ApiModelProperty("用户二级域名")
    private String indexUrl;
    @ApiModelProperty("用户域名")
    private String domain;
    @ApiModelProperty("用户默认主题id")
    private Long defaultThemeId;
    @ApiModelProperty("用户默认主题信息")
    private MemberThemeSimpleData memberThemeSimpleData;
    private List<FactorPortalDao> factorPortalDaos;
    private Date createDt;

    public static UserPortalBean transferToBean(UserPortalDao userPortalDao){
        UserPortalBean userPortalBean = new UserPortalBean();
        BeanUtils.copyProperties(userPortalDao,userPortalBean);
        if(userPortalDao.getFactorPortalDaos()!=null && userPortalDao.getFactorPortalDaos().size()>1){
            userPortalBean.setAllMsg(JSON.toJSONString(userPortalDao.getFactorPortalDaos()));
        }
        return userPortalBean;
    }
}