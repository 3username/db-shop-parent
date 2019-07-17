package com.db.pay.service.impl;

import com.db.base.BaseApiService;
import com.db.mapper.MapperUtils;
import com.db.pay.mapper.PaymentChannelEntityMapper;
import com.db.pay.model.PaymentChannelEntity;
import com.db.pay.output.dto.PaymentChannelDTO;
import com.db.pay.service.PaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PaymentChannelServiceImpl extends BaseApiService<List<PaymentChannelDTO>>
        implements PaymentChannelService {
    private final PaymentChannelEntityMapper paymentChannelMapper;
    @Autowired
    public PaymentChannelServiceImpl(PaymentChannelEntityMapper paymentChannelMapper) {
        this.paymentChannelMapper = paymentChannelMapper;
    }

    @Override
    public List<PaymentChannelDTO> selectAll() {
        List<PaymentChannelEntity> paymentChanneList = paymentChannelMapper.selectAll();
        return MapperUtils.mapAsList(paymentChanneList, PaymentChannelDTO.class);
    }
}