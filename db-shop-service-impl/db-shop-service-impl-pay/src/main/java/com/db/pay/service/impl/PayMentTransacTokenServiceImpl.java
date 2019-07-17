package com.db.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseApiService;
import com.db.base.BaseResponse;
import com.db.core.token.GenerateToken;
import com.db.pay.input.dto.PayCratePayTokenDto;
import com.db.pay.mapper.PaymentTransactionEntityMapper;
import com.db.pay.model.PaymentTransactionEntity;
import com.db.pay.service.PayMentTransacTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayMentTransacTokenServiceImpl extends BaseApiService<JSONObject> implements PayMentTransacTokenService {
    private final PaymentTransactionEntityMapper paymentTransactionEntityMapper;
    private final GenerateToken generateToken;
    @Autowired
    public PayMentTransacTokenServiceImpl(PaymentTransactionEntityMapper paymentTransactionEntityMapper, GenerateToken generateToken) {
        this.paymentTransactionEntityMapper = paymentTransactionEntityMapper;
        this.generateToken = generateToken;
    }

    @Override
    public BaseResponse<JSONObject> cratePayToken(PayCratePayTokenDto payCratePayTokenDto) {
        String orderId = payCratePayTokenDto.getOrderId();
        if(StringUtils.isBlank(orderId)){
            return setResultError("订单号码不能为空！");
        }
        Long payAmount = payCratePayTokenDto.getPayAmount();
        if(payAmount == null){
            return setResultError("金额不能为空");
        }
        Long userId = payCratePayTokenDto.getUserId();
        if(userId == null){
            return setResultError("userId不能为空");
        }
        // 2.将输入插入数据库中 待支付记录
        PaymentTransactionEntity paymentTransactionEntity = new PaymentTransactionEntity();
        paymentTransactionEntity.setOrderId(orderId);
        paymentTransactionEntity.setPayAmount(payAmount);
        paymentTransactionEntity.setUserId(userId);
        //支付状态 0待支付1已经支付2支付超时3支付失败
        paymentTransactionEntity.setPaymentStatus(0);
        // 使用雪花算法 生成全局id
        paymentTransactionEntity.setPaymentId(com.db.twitter.SnowflakeIdUtils.nextId());
        int result = paymentTransactionEntityMapper.insert(paymentTransactionEntity);
        if (!toDaoResult(result)) {
            return setResultError("系统错误!");
        }
        // 获取主键id
        Long payId = paymentTransactionEntity.getId();
        if (payId == null) {
            return setResultError("系统错误!");
        }
        // 3.生成对应支付令牌
        String keyPrefix = "pay_";
        String token = generateToken.createToken(keyPrefix, payId + "");
        JSONObject dataResult = new JSONObject();
        dataResult.put("token", token);
        return setResultSuccess(dataResult);
    }
}















