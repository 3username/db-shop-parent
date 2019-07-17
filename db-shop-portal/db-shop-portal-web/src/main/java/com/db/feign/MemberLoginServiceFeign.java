package com.db.feign;

import com.db.member.service.MemberLoginService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-db-member")
public interface MemberLoginServiceFeign extends MemberLoginService {
}
