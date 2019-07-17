package com.db.pay.mapper;

import com.db.pay.model.PaymentTransactionEntity;
import org.apache.ibatis.annotations.Param;

public interface PaymentTransactionEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentTransactionEntity record);

    int insertSelective(PaymentTransactionEntity record);

    PaymentTransactionEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentTransactionEntity record);

    int updateByPrimaryKey(PaymentTransactionEntity record);

    /**根据paymentId查找*/
    public PaymentTransactionEntity selectByPaymentId(String paymentId);

    /**更新状态*/
    public int updatePaymentStatus(@Param("paymentStatus") Integer paymentStatus, @Param("paymentId") String paymentId);
}