package com.db.member.service;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseResponse;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *用户登陆接口服务
 */
@Api(tags = "用户登陆接口服务")
public interface MemberLoginService {

    /**
     * 用户登陆接口
     * @param userLoginInpDTO
     * @return 登录信息
     */
    @PostMapping("/login")
    @ApiOperation(value = "会员用户登陆信息接口")
    BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO);
}
