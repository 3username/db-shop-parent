package com.db.member.service.impl;

import com.db.base.BaseApiService;
import com.db.base.BaseResponse;
import com.db.constants.Constants;
import com.db.constants.MemberConstants;
import com.db.core.bean.MeiteBeanUtils;
import com.db.core.token.GenerateToken;
import com.db.core.utils.MD5Util;
import com.db.member.feign.AppServiceFeign;
import com.db.member.mapper.UserMapper;
import com.db.member.model.User;
import com.db.member.service.MemberService;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import com.mayikt.member.output.dto.UserOutDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberServiceImpl extends BaseApiService implements MemberService {
    public final AppServiceFeign appServiceFeign;
    public final UserMapper userMapper;
    private final GenerateToken generateToken;

    @Autowired
    public MemberServiceImpl(AppServiceFeign appServiceFeign, UserMapper userMapper, GenerateToken generateToken) {
        this.appServiceFeign = appServiceFeign;
        this.userMapper = userMapper;
        this.generateToken = generateToken;
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

    @Override
    public BaseResponse<UserOutDTO> getInfo(String token) {
        //验证参数
        if(StringUtils.isBlank(token)){
            return setResultError("token不能为空");
        }
        //根据token查Redis
        String redisUserId = generateToken.getToken(token);
        if(StringUtils.isBlank(redisUserId)){
            return setResultError("token已失效");
        }
        //根据ID查找用户信息
        Long id = Long.valueOf(redisUserId);
        User user = userMapper.selectByPrimaryKey(id);
        if(user == null){
            return setResultError(Constants.HTTP_RES_CODE_EXISIMOBILE_203,"查找不到用户信息");
        }
        UserOutDTO userOutDTO = MeiteBeanUtils.doToDto(user, UserOutDTO.class);

        return setResultSuccess(userOutDTO);
    }

    @Override
    public BaseResponse<UserOutDTO> ssoLogin(@RequestBody UserLoginInpDTO userLoginInpDTO) {
        // 1.验证参数
        String mobile = userLoginInpDTO.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号码不能为空!");
        }
        String password = userLoginInpDTO.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultError("密码不能为空!");
        }
        // 判断登陆类型
        String loginType = userLoginInpDTO.getLoginType();
        if (StringUtils.isEmpty(loginType)) {
            return setResultError("登陆类型不能为空!");
        }
        // 目的是限制范围
        if (!(loginType.equals(MemberConstants.MEMBER_LOGIN_TYPE_ANDROID) || loginType.equals(MemberConstants.MEMBER_LOGIN_TYPE_IOS)
                || loginType.equals(MemberConstants.MEMBER_LOGIN_TYPE_PC))) {
            return setResultError("登陆类型出现错误!");
        }

        // 设备信息
        String deviceInfor = userLoginInpDTO.getDeviceInfor();
        if (StringUtils.isEmpty(deviceInfor)) {
            return setResultError("设备信息不能为空!");
        }
        // 2.对登陆密码实现加密
        String newPassWord = MD5Util.MD5(password+mobile);
        // 3.使用手机号码+密码查询数据库 ，判断用户是否存在
        User user = userMapper.login(mobile, newPassWord);
        if (user == null) {
            return setResultError("用户名称或者密码错误!");
        }
        return setResultSuccess(MeiteBeanUtils.doToDto(user, UserOutDTO.class));
    }
}
