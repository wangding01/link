package com.cn.linka.business.wxpay;

import lombok.Builder;
import lombok.Data;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-05-07 11:14
 */
@Data
@Builder
public class WxPayDto {
    private String openid;
    private String outTradeNo;
    private String body;
    private Integer totalFee;
    private String realIp;


}