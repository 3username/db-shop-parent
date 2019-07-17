package com.db.pay.strategy.impl;


import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.config.AlipayConfig;
import com.db.pay.model.PaymentChannelEntity;
import com.db.pay.output.dto.PayMentTransacDTO;
import com.db.pay.strategy.PayStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 支付宝支付渠道
 */
@Slf4j
public class AliPayStrategy implements PayStrategy {

	//com.db.pay.strategy.impl.AliPayStrategy
    @Override
    public String toPayHtml(PaymentChannelEntity pymentChannel, PayMentTransacDTO payMentTransacDTO) {
        log.info(">>>>>支付宝参数封装开始<<<<<<<<");

        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);

        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(pymentChannel.getSyncUrl());
        alipayRequest.setNotifyUrl(pymentChannel.getAsynUrl());

        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = pymentChannel.getMerchantId();
        // 付款金额，必填
        String total_amount = (payMentTransacDTO.getPayAmount() / 100) + "";
        // 订单名称，必填
        String subject = "蚂蚁课堂会员充值";
        // 商品描述，可空
        String body = "超值购物信息";

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"total_amount\":\"" + total_amount
                + "\"," + "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        // 若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        // alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no
        // +"\","
        // + "\"total_amount\":\""+ total_amount +"\","
        // + "\"subject\":\""+ subject +"\","
        // + "\"body\":\""+ body +"\","
        // + "\"timeout_express\":\"10m\","
        // + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        // 请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        // 请求
        try {
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            return result;
        } catch (Exception e) {
            return null;
        }

    }

}