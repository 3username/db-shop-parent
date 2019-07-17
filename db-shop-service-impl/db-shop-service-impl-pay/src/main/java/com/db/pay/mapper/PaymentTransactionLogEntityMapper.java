package com.db.pay.mapper;

import com.db.pay.model.PaymentTransactionLogEntity;

public interface PaymentTransactionLogEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentTransactionLogEntity record);

    int insertSelective(PaymentTransactionLogEntity record);

    PaymentTransactionLogEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentTransactionLogEntity record);

    int updateByPrimaryKeyWithBLOBs(PaymentTransactionLogEntity record);

    int updateByPrimaryKey(PaymentTransactionLogEntity record);
}