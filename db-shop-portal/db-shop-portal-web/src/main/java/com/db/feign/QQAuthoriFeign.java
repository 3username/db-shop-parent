package com.db.feign;

import com.db.member.service.QQAuthoriService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-db-member")
public interface QQAuthoriFeign extends QQAuthoriService {

}
