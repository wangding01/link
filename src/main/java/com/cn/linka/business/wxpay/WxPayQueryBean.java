package com.cn.linka.business.wxpay;

import lombok.Builder;
import lombok.Data;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-05-09 11:20
 */
@Data
@Builder
public class WxPayQueryBean {
    private String orderId;
    private String openId;
    private String tradeState;

    public static WxPayQueryBean nullResponse() {
        return WxPayQueryBean.builder().tradeState("SYSTEM-BUILD").build();
    }
}