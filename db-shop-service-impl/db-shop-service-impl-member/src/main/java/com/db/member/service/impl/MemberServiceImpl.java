package com.db.member.service.impl;

import com.db.base.BaseApiService;
import com.db.base.BaseResponse;
import com.db.constants.Constants;
import com.db.core.bean.MeiteBeanUtils;
import com.db.core.utils.Assert;
import com.db.entity.AppEntity;
import com.db.member.feign.AppServiceFeign;
import com.db.member.mapper.UserMapper;
import com.db.member.model.User;
import com.db.member.service.MemberService;
import com.mayikt.member.output.dto.UserOutDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberServiceImpl extends BaseApiService implements MemberService {
    public final AppServiceFeign appServiceFeign;
    public final UserMapper userMapper;

    @Autowired
    public MemberServiceImpl(AppServiceFeign appServiceFeign, UserMapper userMapper) {
        this.appServiceFeign = appServiceFeign;
        this.userMapper = userMapper;
    }


    @Override
    public BaseResponse<UserOutDTO> existMobile(String mobile) {
        // 1.验证参数
        if(StringUtils.isBlank(mobile)){
            return setResultError("手机号码不能为空");
        }
        // 2.根据手机号码查询用户信息 单独定义code 表示是用户信息不存在把
        User user = userMapper.existMobile(mobile);
        if(user == null){
            return setResultError(Constants.HTTP_RES_CODE_201,"用户信息不存在");
        }
        // 3.将do转换成dto 返回
        return setResultSuccess(MeiteBeanUtils.doToDto(user,UserOutDTO.class));
    }
}
