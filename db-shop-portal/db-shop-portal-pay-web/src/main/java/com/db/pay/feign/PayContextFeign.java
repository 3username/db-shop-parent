package com.db.pay.feign;


import com.db.pay.service.PayContextService;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("app-db-pay")
public interface PayContextFeign extends PayContextService {

}
