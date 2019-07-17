package com.db.pay.mapper;

import com.db.pay.model.PaymentChannelEntity;

import java.util.List;

public interface PaymentChannelEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PaymentChannelEntity record);

    int insertSelective(PaymentChannelEntity record);

    PaymentChannelEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentChannelEntity record);

    int updateByPrimaryKeyWithBLOBs(PaymentChannelEntity record);

    int updateByPrimaryKey(PaymentChannelEntity record);

    /**查找所有支付渠道*/
    List<PaymentChannelEntity> selectAll();

    /**根据渠道id值查找支付渠道信息*/
    PaymentChannelEntity selectBychannelId(String channelId);
}