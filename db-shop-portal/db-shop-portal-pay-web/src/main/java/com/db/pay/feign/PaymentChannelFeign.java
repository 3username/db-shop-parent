package com.db.pay.feign;

import com.db.pay.service.PaymentChannelService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-db-pay")
public interface PaymentChannelFeign extends PaymentChannelService {
}
