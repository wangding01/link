package com.cn.linka.business.dao;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @program: linka
 * @description: 用户主页信息
 * @author: wangding
 * @create: 2022-04-11 14:41
 */
@Data
public class UserPortalDao {
    private String userId;
    private String indexUrl;
    private String domain;
    private String allMsg;
    private Date createDt;
}