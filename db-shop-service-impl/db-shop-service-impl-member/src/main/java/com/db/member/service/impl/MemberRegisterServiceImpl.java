package com.db.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseApiService;
import com.db.base.BaseResponse;
import com.db.constants.Constants;
import com.db.core.bean.MeiteBeanUtils;
import com.db.core.utils.Assert;
import com.db.core.utils.MD5Util;
import com.db.member.feign.VerificaCodeServiceFegin;
import com.db.member.mapper.UserMapper;
import com.db.member.model.User;
import com.db.member.service.MemberRegisterService;
import com.mayikt.member.input.dto.UserInpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberRegisterServiceImpl extends BaseApiService implements MemberRegisterService {

    private final UserMapper userMapper;
    private final VerificaCodeServiceFegin verificaCodeServiceFegin;
    @Autowired
    public MemberRegisterServiceImpl(UserMapper userMapper, VerificaCodeServiceFegin verificaCodeServiceFegin) {
        this.userMapper = userMapper;
        this.verificaCodeServiceFegin = verificaCodeServiceFegin;
    }

    @Override
    public BaseResponse<JSONObject> register(@RequestBody UserInpDTO userInpDTO, String registCode) {
        //1.验证参数
        String mobile = userInpDTO.getMobile();
        String email = userInpDTO.getEmail();
        String password = userInpDTO.getPassword();
        Assert.notStringNull(mobile,"手机号不能为空");
        //2.验证验证码是否正确
        BaseResponse<JSONObject> verificaWeixinCode =
                verificaCodeServiceFegin.verificaWeixinCode(mobile, registCode);
        if(!Constants.HTTP_RES_CODE_200.equals(verificaWeixinCode.getCode())){
            return setResultError(verificaWeixinCode.getMsg());
        }
        //3.对密码加密加盐
        String newPassword = MD5Util.MD5(password + mobile);
        userInpDTO.setPassword(newPassword);
        //4.调用数据库插入数据
        User user = MeiteBeanUtils.dtoToDo(userInpDTO, User.class);
        user.setSex('0');
        user.setIsAvalible('1');
        return userMapper.insert(user) > 0 ? setResultSuccess("注册成功") : setResultError("注册失败");
    }
}
