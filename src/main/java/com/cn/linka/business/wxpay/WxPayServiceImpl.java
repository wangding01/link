package com.cn.linka.business.wxpay;

import com.alibaba.fastjson.JSON;
import com.cn.linka.business.bean.UserOrderBean;
import com.cn.linka.business.mapper.UserOrderMapper;
import com.cn.linka.business.service.UserOrderService;
import com.cn.linka.business.wxpay.sdkUtil.WXPayUtil;
import com.cn.linka.common.exception.BusException;
import com.cn.linka.common.exception.BusinessExceptionEnum;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by xxs on 2021/7/30 9:56
 *
 * @Description
 * @Version 2.9
 */
@Slf4j
@Service
public class WxPayServiceImpl implements WxPayService {

    @Resource
    private UserOrderService userOrderService;
    public String appId;

    public String mch_id;

    public String notify_url;

    public String key;

    @Value("${link.appid}")
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
    public String nativePay(WxPayDto dto){
        log.info("??????native??????, ??????:{}", JSON.toJSONString(dto));
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
        packageParams.put("trade_type", "NATIVE");
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", outTradeNo);
        packageParams.put("total_fee", totalFee + "");
        Map map = null;
        try {
            String xml = WXPayUtil.generateSignedXml(packageParams, key);
            log.info(xml);
            String result = CommUtils.httpRequest(CommUtils.unifiedOrderUrl, "POST", xml);
            map = CommUtils.doXMLParse(result);
        }catch (Exception e){
            log.error("??????????????????");
            throw new BusException(BusinessExceptionEnum.WX_PAY_ORDER_FAIL);
        }
        log.info("??????????????????map???" + map.toString());
        Object return_code = map.get("return_code");
        Object return_msg = map.get("return_msg");
        if ("SUCCESS".equals(return_code) &&  "OK".equals(return_msg)) {
            log.info("????????????????????????");
            String code_url = (String) map.get("code_url");
            generateQRCode(code_url);
            return code_url;
        } else {
            log.error("?????????????????????{}",map.toString());
            throw new BusException(BusinessExceptionEnum.WX_PAY_ORDER_FAIL);
        }
    }


    /**
     * @param request
     * @param response
     * @Author: xxs
     * @Date: 2021/7/31 15:17
     * @Description: ??????????????????
     * @Version: 2.9
     * @Return: void
     */
    @Transactional
    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("??????????????????");
        String resXml = "";
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        String notityXml = sb.toString();
        log.info("???????????????????????????" + notityXml);
        if (!WXPayUtil.isSignatureValid(notityXml, key)) {
            throw new BusException(BusinessExceptionEnum.WX_PAY_BACK_SIGN_FAIL);
        }
        Map map = CommUtils.doXMLParse(notityXml);
        Object returnCode = map.get("return_code");
        Object result_code = map.get("result_code");
        if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(result_code)) {
            String order_no = (String) map.get("out_trade_no");
            String otherId = (String) map.get("openid");
            Optional<UserOrderBean> userOrderBean = userOrderMapper.queryByOrderId(order_no);
            if(userOrderBean.isPresent() && "1".equals(userOrderBean.get().getOrderStatus())){
                if(StringUtils.isEmpty(userOrderBean.get().getOrderId())){
                    log.info("????????????{}??????????????????openid:{}",order_no,otherId);
                    userOrderMapper.syncOtherId(order_no,otherId);
                }
                log.info("?????????????????????????????????????????????????????????id???{}",order_no);
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(CommUtils.SUCCESSxml.getBytes());
                out.flush();
                out.close();
                return;
            }
            //???????????????????????????
            userOrderService.completeOrder(order_no,otherId);
            resXml = CommUtils.SUCCESSxml;
        } else {
            resXml = CommUtils.ERRORxml;
        }
        log.info("?????????????????????????????????" + resXml);
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    @Override
    public WxPayQueryBean checkWxPayOrder(String oderId){
        log.info("???????????????????????????");
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
            log.error("??????????????????????????????");
        }
        log.info("??????????????????map???" + map.toString());
        Object return_code = map.get("return_code");
        Object return_msg = map.get("return_msg");
        String openId = (String) map.get("openid");
        String tradeState =  (String) map.get("trade_state");
        if ("SUCCESS".equals(return_code) &&  "OK".equals(return_msg)) {
            log.info("??????????????????");
            return WxPayQueryBean.builder()
                    .orderId(oderId)
                    .tradeState(tradeState)
                    .openId(openId).build();
        } else {
            log.info("??????????????????");
            throw new BusException(BusinessExceptionEnum.WX_PAY_ORDER_CHECK_ERROR);
        }

    }

    /**
     * ???????????????
     * @throws WriterException
     * @throws IOException
     */
    public static void generateQRCode(String code_url) {
        //???????????????
        //????????????????????????
        int width = 200;
        int hight = 200;
        //??????map
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        //?????????????????? ?????????????????????
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(code_url, BarcodeFormat.QR_CODE, width, hight, hints);

            //???????????????????????????
            String filePath = "D:\\JAVA\\link\\code";
            String fileName = RandomStringUtils.randomAlphanumeric(10) + ".jpg";

            Path path = FileSystems.getDefault().getPath(filePath, fileName);

            //?????????????????????????????????
            MatrixToImageWriter.writeToPath(bitMatrix, "jpg", path);
        } catch (Exception e) {
            log.error("?????????????????????");
        }
    }
}