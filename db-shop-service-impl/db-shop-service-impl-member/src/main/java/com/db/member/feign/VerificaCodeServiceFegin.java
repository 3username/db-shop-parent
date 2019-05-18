package com.db.member.feign;

import com.db.service.VerificaCodeService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("app-db-weixin")
public interface VerificaCodeServiceFegin extends VerificaCodeService {
}
