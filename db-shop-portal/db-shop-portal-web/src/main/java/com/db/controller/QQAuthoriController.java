package com.db.controller;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseResponse;
import com.db.constants.Constants;
import com.db.constants.MemberConstants;
import com.db.constants.WebConstants;
import com.db.controller.req.vo.LoginVo;
import com.db.feign.MemberLoginServiceFeign;
import com.db.feign.QQAuthoriFeign;
import com.db.web.base.BaseWebController;
import com.db.web.bean.MeiteBeanUtils;
import com.db.web.utils.CookieUtils;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * qq授权
 */
@Slf4j
@Controller
public class QQAuthoriController extends BaseWebController {

    /**重定向到首页*/
    private static final String REDIRECT_INDEX = "redirect:/";
    /**qq登录*/
    private static final String MB_QQ_QQLOGIN = "member/qqlogin";

    private final MemberLoginServiceFeign memberLoginServiceFeign;
    private final QQAuthoriFeign qqAuthoriFeign;
    @Autowired
    public QQAuthoriController(MemberLoginServiceFeign memberLoginServiceFeign,
                               QQAuthoriFeign qqAuthoriFeign) {
        this.memberLoginServiceFeign = memberLoginServiceFeign;
        this.qqAuthoriFeign = qqAuthoriFeign;
    }

    /**
     * 生成QQ授权链接
     * @param request
     * @return
     */
    @RequestMapping("/qqAuth")
    public String qqAuth(HttpServletRequest request){
        try {
            String authorizeURL = new Oauth().getAuthorizeURL(request);
            log.info("authorizeURL:{}", authorizeURL);
            return "redirect:" + authorizeURL;
        }catch (Exception e){
            log.error("QQAuthoriController.qqAuth;error:{}",e.getMessage());
            return ERROR_500_FTL;
        }
    }

    /**
     * 活动授权回调的code 回调地址
     * @param code
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping("/qqLoginBack")
    public String qqLoginBack(String code, HttpServletRequest request,
                              HttpServletResponse response, HttpSession session){
        try {
            //使用授权码获取accessToken
            AccessToken accessTokenObj = (new Oauth())
                    .getAccessTokenByRequest(request);
            if(accessTokenObj == null){
                return ERROR_500_FTL;
            }
            String accessToken = accessTokenObj.getAccessToken();
            if(StringUtils.isBlank(accessToken)){
                return ERROR_500_FTL;
            }
            //使用AccessToken获取用户openId
            OpenID openIDObj = new OpenID(accessToken);
            String userOpenID = openIDObj.getUserOpenID();
            if(StringUtils.isBlank(userOpenID)){
                return ERROR_500_FTL;
            }
            //使用openID查找数据 是否已关联账号
            BaseResponse<JSONObject> findByOpenId =
                    qqAuthoriFeign.findByOpenId(userOpenID);
            if(!isSuccess(findByOpenId)){
                return ERROR_500_FTL;
            }
            // 如果接口返回203, 跳转到关联账号页面
            if(findByOpenId.getCode().equals(Constants.HTTP_RES_CODE_NOTUSER_203)){
                //页面需要展示 QQ头像
                UserInfo userInfo = new UserInfo(accessToken, userOpenID);
                UserInfoBean userInfoBean = userInfo.getUserInfo();
                if(userInfoBean == null){
                    return ERROR_500_FTL;
                }
                String avatarURL100 = userInfoBean.getAvatar().getAvatarURL100();
                request.setAttribute("avatarURL100",avatarURL100);
                //需要将openid存到session中
                session.setAttribute(WebConstants.LOGIN_QQ_OPENID,userOpenID);
                return MB_QQ_QQLOGIN;
            }
            // 如果能够查询到用户信息的话,直接自动登陆
            JSONObject data = findByOpenId.getData();
            String token = data.getString("token");
            CookieUtils.setCookie(request,response, WebConstants.LOGIN_TOKEN_COOKIENAME,token);
            return REDIRECT_INDEX;
        }catch (Exception e){
            return ERROR_500_FTL;
        }
    }

    /**
     * 用户登录,并且绑定QQ
     * @param loginVo
     * @param model
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping("/qqJointLogin")
    public String qqJointLogin(@ModelAttribute("loginVo") LoginVo loginVo, Model model,
                               HttpServletRequest request, HttpServletResponse
                                       response, HttpSession session){
        //获取用户的openID
        String openId = (String) session.getAttribute(WebConstants.LOGIN_QQ_OPENID);
        if(StringUtils.isBlank(openId)){
            return ERROR_500_FTL;
        }
        //将vo转换成dto 调用会员登录接口
        UserLoginInpDTO userLoginInpDTO = MeiteBeanUtils.voToDto(loginVo, UserLoginInpDTO.class);
        userLoginInpDTO.setQqOpenId(openId);
        userLoginInpDTO.setLoginType(MemberConstants.MEMBER_LOGIN_TYPE_PC);
        String info = webBrowserInfo(request);
        userLoginInpDTO.setDeviceInfor(info);
        BaseResponse<JSONObject> login = memberLoginServiceFeign.login(userLoginInpDTO);
        if(!isSuccess(login)){
            setErrorMsg(model,login.getMsg());
            return MB_QQ_QQLOGIN;
        }
        //登陆成功之后如何处理 保持会话信息 将token存入到cookie 里面 首页读取cookietoken 查询用户信息返回到页面展示
        JSONObject data = login.getData();
        String token = data.getString("token");
        CookieUtils.setCookie(request,response,WebConstants.LOGIN_TOKEN_COOKIENAME,token);
        return REDIRECT_INDEX;
    }

}
