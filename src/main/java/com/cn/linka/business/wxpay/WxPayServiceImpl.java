package com.cn.linka.business.wxpay;

import com.alibaba.fastjson.JSON;
import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.UserOrderDao;
import com.cn.linka.business.mapper.UserOrderMapper;
import com.cn.linka.business.service.MemberMenuService;
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
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", outTradeNo);
////        double t = DoubleUtil.parseDouble(totalFee);//保留两位小数
//        int aDouble = Integer.parseInt(new java.text.DecimalFormat("0").format(t*100));
        packageParams.put("total_fee", totalFee + "");
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", "MWEB");
        packageParams.put("scene_info", scene_info);
//        packageParams.put("openid", openid);

        packageParams = CommUtils.paraFilter(packageParams);
        String prestr = CommUtils.createLinkString(packageParams);
        String sign = CommUtils.sign(prestr, key, "utf-8").toUpperCase();
        log.info("统一下单请求签名：" + sign);
        String xml = "<xml version='1.0' encoding='gbk'>" + "<appid>" + appId + "</appid>"
                + "<body><![CDATA[" + body + "]]></body>"
                + "<mch_id>" + mch_id + "</mch_id>"
                + "<nonce_str>" + nonce_str + "</nonce_str>"
                + "<notify_url>" + notify_url + "</notify_url>"
                + "<openid>" + openid + "</openid>"
                + "<out_trade_no>" + outTradeNo + "</out_trade_no>"
                + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                + "<total_fee>" + totalFee + "" + "</total_fee>"
                + "<trade_type>" + "MWEB" + "</trade_type>"
                + "<sign>" + sign + "</sign>"
                + "<scene_info>" + scene_info + "</scene_info>"
                + "</xml>";

        String result = CommUtils.httpRequest(CommUtils.unifiedOrderUrl, "POST", xml);
        Map map = CommUtils.doXMLParse(result);
        log.info("统一下单返回map：" + map.toString());
        Object return_code = map.get("return_code");
        Object return_msg = map.get("return_msg");
//        if(return_code == "SUCCESS"  || return_code.equals(return_code)){
//            Map<String,String> resultMap=new HashMap<String, String>();
//            String prepay_id = (String) map.get("prepay_id");
//            resultMap.put("appId", appId);
//            Long timeStamp = System.currentTimeMillis() / 1000;
//            resultMap.put("timeStamp", timeStamp + "");
//            resultMap.put("nonceStr", nonce_str);
//            resultMap.put("package", "prepay_id=" + prepay_id);
//            resultMap.put("signType", "MD5");
//            log.info("参与paySign签名数据, 入参:{}", JSON.toJSONString(resultMap));
//            String linkString = CommUtils.createLinkString(resultMap);
//            String paySign = CommUtils.sign(linkString, key, "utf-8").toUpperCase();
//            log.info("获取到paySign:"+paySign);
//            resultMap.put("paySign", paySign);
//            return BaseDaoForHttp.success();
//        }
//        return BaseDaoForHttp.fail();
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
        Map map = CommUtils.doXMLParse(notityXml);
        Object returnCode = map.get("return_code");
        Object result_code = map.get("result_code");
        if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(result_code)) {
            Map<String, String> validParams = CommUtils.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String validStr = CommUtils.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String sign = CommUtils.sign(validStr, key, "utf-8").toUpperCase();//拼装生成服务器端验证的签名
            log.info("支付回调生成签名：" + sign);
//            String transaction_id = (String) map.get("transaction_id");
            String order_no = (String) map.get("out_trade_no");
//            String time_end = (String) map.get("time_end");
//            String total_fee = (String) map.get("total_fee");
            //签名验证
            if (!sign.equals(map.get("sign"))) {
                throw new BusException(BusinessExceptionEnum.WX_PAY_BACK_SIGN_FAIL);
            }
            //修改本地数据库操作
            if (!(userOrderMapper.updateStatus(order_no) == 1)) {
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
}