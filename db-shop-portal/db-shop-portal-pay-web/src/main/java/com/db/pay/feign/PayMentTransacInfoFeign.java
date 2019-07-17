package com.db.pay.feign;

import org.springframework.cloud.openfeign.FeignClient;

import com.db.pay.service.PayMentTransacInfoService;

@FeignClient("app-db-pay")
public interface PayMentTransacInfoFeign extends PayMentTransacInfoService {

}
