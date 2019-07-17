package com.db.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseApiService;
import com.db.base.BaseResponse;
import com.db.constants.MemberConstants;
import com.db.core.token.GenerateToken;
import com.db.core.transaction.RedisDataSoureceTransaction;
import com.db.core.utils.MD5Util;
import com.db.member.mapper.UserMapper;
import com.db.member.mapper.UserTokenMapper;
import com.db.member.model.User;
import com.db.member.model.UserToken;
import com.db.member.service.MemberLoginService;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
public class MemberLoginServiceImpl extends BaseApiService<JSONObject> implements MemberLoginService {

    private final UserMapper userMapper;
    private final GenerateToken generateToken;
    private final UserTokenMapper userTokenMapper;
    /**手动事务工具类*/
    private final RedisDataSoureceTransaction redisDataSoureceTransaction;

    public MemberLoginServiceImpl(UserMapper userMapper, GenerateToken generateToken, UserTokenMapper userTokenMapper, RedisDataSoureceTransaction redisDataSoureceTransaction) {
        this.userMapper = userMapper;
        this.generateToken = generateToken;
        this.userTokenMapper = userTokenMapper;
        this.redisDataSoureceTransaction = redisDataSoureceTransaction;
    }

    @Override
    public BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO) {
        //验证参数
        String mobile = userLoginInpDTO.getMobile();
        String password = userLoginInpDTO.getPassword();
        if(StringUtils.isBlank(mobile)){
            return setResultError("手机号码不能为空");
        }
        if(StringUtils.isBlank(password)){
            return setResultError("密码不能为空");
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

        //对登陆密码实现加密
        String newPassword = MD5Util.MD5(password + mobile);
        //使用手机号码+密码查询数据库 ，判断用户是否存在
        User user = userMapper.login(mobile, newPassword);
        if(user == null){
            return setResultError("手机号码或密码不正确");
        }

        TransactionStatus transactionStatus = null;
        try {
            // 用户登陆Token Session 区别
            // 用户每一个端登陆成功之后，会对应生成一个token令牌（临时且唯一）存放在redis中作为rediskey value userid
            // 4.获取userid
            Long userId = user.getUserId();

            // 5.根据userId+loginType 查询当前登陆类型账号之前是否有登陆过，如果登陆过 清除之前redistoken
            UserToken userTokenDo = userTokenMapper.selectByUserIdAndLoginType(userId, loginType);

            transactionStatus = redisDataSoureceTransaction.begin();
            if (userTokenDo != null) {
                // 如果登陆过 清除之前redistoken
                //String token = userTokenDo.getToken();
                //Boolean isremoveToken = generateToken.removeToken(token);
                //if (isremoveToken) {
                    // 把该token的状态改为删除
                    //userTokenMapper.updateTokenAvailability(token);
                //}
                // 如果登陆过 清除之前redistoken
                String oriToken = userTokenDo.getToken();
                // 移除Token
                generateToken.removeToken(oriToken);
                int updateTokenAvailability = userTokenMapper.updateTokenAvailability(oriToken);
                if (updateTokenAvailability < 0) {
                    redisDataSoureceTransaction.rollback(transactionStatus);
                    return setResultError("系统错误");
                }


            }

            // 如果有传递openid参数，修改到数据中
            String qqOpenId = userLoginInpDTO.getQqOpenId();
            if (!StringUtils.isEmpty(qqOpenId)) {
                userMapper.updateUserOpenId(qqOpenId, userId);
            }

            // .生成对应用户令牌存放在redis中
            String keyPrefix = MemberConstants.MEMBER_TOKEN_KEYPREFIX + loginType;
            String newToken = generateToken.createToken(keyPrefix, userId + "");

            // 插入新的token
            UserToken userToken = new UserToken();
            userToken.setUserId(userId);
            userToken.setLoginType(userLoginInpDTO.getLoginType());
            userToken.setToken(newToken);
            userToken.setDeviceInfor(deviceInfor);
            userToken.setActive(true);
            userToken.setCreateTime(new Date());
            userToken.setUpdateTime(new Date());
            int result = userTokenMapper.insertUserToken(userToken);
            if (!toDaoResult(result)) {
                redisDataSoureceTransaction.rollback(transactionStatus);
                return setResultError("系统错误!");
            }


            JSONObject data = new JSONObject();
            data.put("token", newToken);

            //提交事务
            redisDataSoureceTransaction.commit(transactionStatus);
            return setResultSuccess(data);
        }catch (Exception e){
            log.info("======系统错误======"+e.getMessage());
            try {
                // 回滚事务
                redisDataSoureceTransaction.rollback(transactionStatus);
            } catch (Exception e1) {
            }
            return setResultError("系统错误!");

        }
    }
    // 查询用户信息的话如何实现？ redis 与数据库如何保证一致问题
}
