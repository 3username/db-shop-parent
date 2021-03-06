package com.db.feign;

import com.db.member.service.MemberService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-db-member")
public interface MemberServiceFeign extends MemberService {
}
