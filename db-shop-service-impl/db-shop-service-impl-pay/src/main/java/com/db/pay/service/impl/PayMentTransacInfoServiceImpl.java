package com.db.pay.service.impl;

import com.db.base.BaseApiService;
import com.db.base.BaseResponse;
import com.db.core.bean.MeiteBeanUtils;
import com.db.core.token.GenerateToken;
import com.db.pay.mapper.PaymentTransactionEntityMapper;
import com.db.pay.model.PaymentTransactionEntity;
import com.db.pay.output.dto.PayMentTransacDTO;
import com.db.pay.service.PayMentTransacInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayMentTransacInfoServiceImpl extends BaseApiService<PayMentTransacDTO> implements PayMentTransacInfoService {

    private final GenerateToken generateToken;
    private final PaymentTransactionEntityMapper paymentTransactionMapper;

    public PayMentTransacInfoServiceImpl(GenerateToken generateToken, PaymentTransactionEntityMapper paymentTransactionMapper) {
        this.generateToken = generateToken;
        this.paymentTransactionMapper = paymentTransactionMapper;
    }

    @Override
    public BaseResponse<PayMentTransacDTO> tokenByPayMentTransac(String token) {
        // 1.验证token是否为空
        if (StringUtils.isEmpty(token)) {
            return setResultError("token参数不能空!");
        }
        // 2.使用token查询redisPayMentTransacID
        String value = generateToken.getToken(token);
        if (StringUtils.isEmpty(value)) {
            return setResultError("该Token可能已经失效或者已经过期");
        }
        // 3.转换为整数类型
        Long transactionId = Long.parseLong(value);
        // 4.使用transactionId查询支付信息
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectByPrimaryKey(transactionId);
        if (paymentTransaction == null) {
            return setResultError("未查询到该支付信息");
        }
        return setResultSuccess(MeiteBeanUtils.doToDto(paymentTransaction, PayMentTransacDTO.class));
    }
}
