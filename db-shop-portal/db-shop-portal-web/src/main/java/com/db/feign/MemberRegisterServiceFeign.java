package com.db.feign;

import com.db.member.service.MemberRegisterService;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("app-db-member")
public interface MemberRegisterServiceFeign extends MemberRegisterService {
}
