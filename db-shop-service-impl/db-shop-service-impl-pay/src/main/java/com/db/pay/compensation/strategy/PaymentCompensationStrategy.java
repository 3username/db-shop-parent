package com.db.pay.compensation.strategy;


import com.db.pay.model.PaymentChannelEntity;
import com.db.pay.model.PaymentTransactionEntity;

/**
 * 
 * 对账抽象策略角色
 * 

 */
public interface PaymentCompensationStrategy {
	/**
	 * 渠道名称
	 * 
	 * @param paymentChannel
	 * @return
	 */
	public Boolean payMentCompensation(PaymentTransactionEntity paymentTransaction, PaymentChannelEntity paymentChanne);
}
