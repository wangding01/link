package com.cn.linka.business.bean;

import com.alibaba.fastjson.JSONObject;
import com.cn.linka.business.dao.FactorPortalDao;
import com.cn.linka.business.dao.UserPortalDao;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
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
public class UserPortalBean {
    private String userId;
    private String indexUrl;
    private String portalHeadUrl;
    private String introduction;
    private String userDetail;
    private String domain;
    private Long defaultThemeId;
    private String allMsg;
    private Date createDt;

    public static UserPortalDao transferToDao(UserPortalBean userPortalBean){
        UserPortalDao userPortalDao = new UserPortalDao();
        BeanUtils.copyProperties(userPortalBean,userPortalDao);
        if(StringUtils.isNotBlank(userPortalBean.getAllMsg())){
            List<FactorPortalDao> factorPortalDaos = JSONObject.parseArray(userPortalBean.getAllMsg(), FactorPortalDao.class);
            userPortalDao.setFactorPortalDaos(factorPortalDaos);
        }
        return userPortalDao;
    }
}