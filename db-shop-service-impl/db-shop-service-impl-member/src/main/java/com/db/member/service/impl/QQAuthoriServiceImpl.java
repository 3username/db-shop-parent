package com.db.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseApiService;
import com.db.base.BaseResponse;
import com.db.constants.Constants;
import com.db.core.token.GenerateToken;
import com.db.member.mapper.UserMapper;
import com.db.member.model.User;
import com.db.member.service.QQAuthoriService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QQAuthoriServiceImpl extends BaseApiService<JSONObject> implements QQAuthoriService {
    private UserMapper userMapper;
    private GenerateToken generateToken;

    @Override
    public BaseResponse<JSONObject> findByOpenId(String qqOpenId) {
        // 1.根据qqOpenId查询用户信息
        if (StringUtils.isEmpty(qqOpenId)) {
            return setResultError("qqOpenId不能为空!");
        }
        // 2.如果没有查询到 直接返回状态码203
        User userDo = userMapper.findByOpenId(qqOpenId);
        if (userDo == null) {
            return setResultError(Constants.HTTP_RES_CODE_NOTUSER_203,
                    "根据qqOpenId没有查询到用户信息");
        }
        // 3.如果能够查询到用户信息的话 返回对应用户信息token
        String keyPrefix = Constants.MEMBER_TOKEN_KEYPREFIX + "QQ_LOGIN";
        Long userId = userDo.getUserId();
        String userToken = generateToken.createToken(keyPrefix, userId + "");
        JSONObject data = new JSONObject();
        data.put("token", userToken);
        return setResultSuccess(data);
    }

}
