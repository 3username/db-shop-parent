package com.db.pay.strategy;

import com.db.pay.model.PaymentChannelEntity;
import com.db.pay.output.dto.PayMentTransacDTO;

/**
 * 支付接口共同实现行为算法
 */
public interface PayStrategy {
    /**
     *
     * @param pymentChannel
     *            渠道参数
     * @param payMentTransacDTO
     *            支付参数
     * @return
     */
    public String toPayHtml(PaymentChannelEntity pymentChannel, PayMentTransacDTO payMentTransacDTO);
}
