package com.db.feign;

import com.db.member.service.MemberService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient(name = "app-db-member")
public interface MemberServiceFeign extends MemberService {
}
