package com.db.pay.callback.template.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.config.AlipayConfig;
import com.db.pay.callback.template.AbstractPayCallbackTemplate;
import com.db.pay.constant.PayConstant;
import com.db.pay.mapper.PaymentTransactionEntityMapper;
import com.db.pay.model.PaymentTransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝异步回调
 */
public class AliPayCallbackTemplate extends AbstractPayCallbackTemplate {
    @Autowired
    private  PaymentTransactionEntityMapper paymentTransactionMapper;


    @Override
    public Map<String, String> verifySignature(HttpServletRequest req, HttpServletResponse resp) {

        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = req.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put(name, valueStr);
        }

        boolean signVerified = false; //调用SDK验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }


        //——请在这里编写您的程序（以下代码仅作参考）——

	/* 实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/
        if(signVerified) {//验证成功
            //商户订单号
            //String out_trade_no = new String(req.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            //String trade_no = new String(req.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //交易状态
            //String trade_status = new String(req.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

            //if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            //}else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
            //}
            String orderId = params.get("orderId"); // 获取后台通知的数据，其他字段也可用类似方式获取
            params.put("paymentId", orderId);
            params.put(PayConstant.RESULT_NAME, PayConstant.RESULT_PAYCODE_200);

        }else {//验证失败
            params.put(PayConstant.RESULT_NAME, PayConstant.RESULT_PAYCODE_201);

            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
        }

        return params;
    }

    @Override
    public String asyncService(Map<String, String> verifySignature) {
        String orderId = verifySignature.get("orderId"); // 获取后台通知的数据，其他字段也可用类似方式获取
        String respCode = verifySignature.get("trade_status");

        // 判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
        System.out.println("orderId:" + orderId + ",respCode:" + respCode);
        // 1.判断respCode是否为已经支付成功断respCode=TRADE_SUCCESS后，
        if (!(respCode.equals("TRADE_SUCCESS"))) {
            return failResult();
        }
        // 根据日志 手动补偿 使用支付id调用第三方支付接口查询
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectByPaymentId(orderId);
        if (paymentTransaction.getPaymentStatus().equals(PayConstant.PAY_STATUS_SUCCESS)) {
            // 网络重试中，之前已经支付过
            return successResult();
        }
        // 2.将状态改为已经支付成功
        paymentTransactionMapper.updatePaymentStatus(PayConstant.PAY_STATUS_SUCCESS , orderId);
        // 3.调用积分服务接口增加积分(处理幂等性问题)
        return successResult();
    }

    @Override
    public String failResult() {
        return PayConstant.ALIPAY_RESULT_FAIL;
    }

    @Override
    public String successResult() {
        return PayConstant.ALIPAY_RESULT_SUCCESS;
    }
}
