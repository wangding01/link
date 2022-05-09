package com.cn.linka.business.wxpay;

import com.alibaba.fastjson.JSON;
import com.cn.linka.business.mapper.UserOrderMapper;
import com.cn.linka.business.wxpay.sdkUtil.WXPayUtil;
import com.cn.linka.common.exception.BusException;
import com.cn.linka.common.exception.BusinessExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xxs on 2021/7/30 9:56
 *
 * @Description
 * @Version 2.9
 */
@Slf4j
@Service
public class WxPayServiceImpl implements WxPayService {
    private static final String scene_info = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://www.link.cn\",\"wap_name\": \"linkcn充值\"}}";
    public String appId;

    public String mch_id;

    public String notify_url;

    public String key;

    @Value("${gzh.appid}")
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Value("${wxPay.mchId}")
    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    @Value("${wxPay.notifyUrl}")
    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    @Value("${wxPay.key}")
    public void setKey(String key) {
        this.key = key;
    }

    @Resource
    private UserOrderMapper userOrderMapper;

    @Override
    public String pay(WxPayDto dto) throws Exception {
        log.info("公众号微信支付, 入参:{}", JSON.toJSONString(dto));
        String openid = dto.getOpenid();
        String outTradeNo = dto.getOutTradeNo();
        String body = dto.getBody();
        Integer totalFee = dto.getTotalFee();
        String nonce_str = CommUtils.getRandomStringByLength(32);
        String spbill_create_ip = dto.getRealIp();
        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appid", appId);
        packageParams.put("mch_id", mch_id);
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", "MWEB");
        packageParams.put("scene_info", scene_info);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", outTradeNo);
        packageParams.put("total_fee", totalFee + "");
        String xml = WXPayUtil.generateSignedXml(packageParams, key);
        log.info(xml);
        String result = CommUtils.httpRequest(CommUtils.unifiedOrderUrl, "POST", xml);
        Map map = CommUtils.doXMLParse(result);
        log.info("统一下单返回map：" + map.toString());
        Object return_code = map.get("return_code");
        Object return_msg = map.get("return_msg");
        if (return_code == "SUCCESS" && return_msg == "OK") {
            log.info("微信支付成功拉起");
            String mweb_url = (String) map.get("mweb_url");
            return mweb_url;
        } else {
            throw new BusException(BusinessExceptionEnum.WX_PAY_ORDER_FAIL);
        }
    }


    /**
     * @param request
     * @param response
     * @Author: xxs
     * @Date: 2021/7/31 15:17
     * @Description: 微信支付回调
     * @Version: 2.9
     * @Return: void
     */
    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("进入支付回调啦啦啦啦*-*");
        String resXml = "";
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        String notityXml = sb.toString();
        log.info("支付回调返回数据：" + notityXml);
        if (!WXPayUtil.isSignatureValid(notityXml, key)) {
            throw new BusException(BusinessExceptionEnum.WX_PAY_BACK_SIGN_FAIL);
        }
        Map map = CommUtils.doXMLParse(notityXml);
        Object returnCode = map.get("return_code");
        Object result_code = map.get("result_code");
        if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(result_code)) {
            String order_no = (String) map.get("out_trade_no");
            String otherId = (String) map.get("open_id");
            //修改本地数据库操作
            if (!(userOrderMapper.updateStatus(order_no,otherId) == 1)) {
                throw new BusException(BusinessExceptionEnum.WX_PAY_BACK_ORDER_FAIL);
            }
            resXml = CommUtils.SUCCESSxml;
        } else {
            resXml = CommUtils.ERRORxml;
        }
        log.info("微信支付回调返回数据：" + resXml);
        log.info("微信支付回调数据结束");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    @Override
    public WxPayQueryBean checkWxPayOrder(String oderId){
        log.info("查询微信支付的订单");
        String nonce_str = CommUtils.getRandomStringByLength(32);
        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appid", appId);
        packageParams.put("mch_id", mch_id);
        packageParams.put("notify_url", notify_url);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("out_trade_no", oderId);
        Map map = null;
        try {
            String xml = WXPayUtil.generateSignedXml(packageParams, key);
            log.info(xml);
            String result = CommUtils.httpRequest(CommUtils.orderquery, "POST", xml);
            map = CommUtils.doXMLParse(result);
        }catch (Exception e){
            e.printStackTrace();
            log.error("微信订单查询调用异常");
        }
        log.info("订单查询返回map：" + map.toString());
        Object return_code = map.get("return_code");
        Object return_msg = map.get("return_msg");
        String openId = (String) map.get("openid");
        String tradeState =  (String) map.get("trade_state");
        if (return_code == "SUCCESS" && return_msg == "OK") {
            log.info("订单支付成功");
            return WxPayQueryBean.builder()
                    .orderId(oderId)
                    .tradeState(tradeState)
                    .openId(openId).build();
        } else {
            log.info("订单支付失败");
            throw new BusException(BusinessExceptionEnum.WX_PAY_ORDER_CHECK_ERROR);
        }

    }
}