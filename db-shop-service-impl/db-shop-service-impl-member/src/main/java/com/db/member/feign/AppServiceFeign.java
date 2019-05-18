package com.db.member.feign;

import com.db.service.AppService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "app-db-weixin")
public interface AppServiceFeign extends AppService {
}
