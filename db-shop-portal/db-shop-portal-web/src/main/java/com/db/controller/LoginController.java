package com.db.controller;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseResponse;
import com.db.constants.MemberConstants;
import com.db.constants.WebConstants;
import com.db.controller.req.vo.LoginVo;
import com.db.feign.MemberLoginServiceFeign;
import com.db.web.base.BaseWebController;
import com.db.web.bean.MeiteBeanUtils;
import com.db.web.utils.CookieUtils;
import com.db.web.utils.RandomValidateCodeUtil;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController extends BaseWebController {
	/**跳转到登陆页面*/
	private final static String MB_LOGIN_FTL = "member/login";
	/**重定向到首页*/
	private final static String REDIRECT_INDEX = "redirect:/";

	private final MemberLoginServiceFeign memberLoginServiceFeign;
	@Autowired
	public LoginController(MemberLoginServiceFeign memberLoginServiceFeign) {
		this.memberLoginServiceFeign = memberLoginServiceFeign;
	}

	/**
	 * 跳转到注册页面
	 * 
	 * @return
	 */
	@GetMapping("/login")
	public String getLogin() {
		return MB_LOGIN_FTL;
	}

	/**
	 * 接收请求参数
	 * @return
	 */
	@PostMapping("/login")
	public String postLogin(LoginVo loginVo, Model model, HttpServletRequest request,
							HttpServletResponse response, HttpSession session) {
		//图形码验证
		String graphicCode = loginVo.getGraphicCode();
		if(!RandomValidateCodeUtil.checkVerify(graphicCode,session)){
			setErrorMsg(model,"图形码错误!");
			return MB_LOGIN_FTL;
		}
		//将vo转换dto调用会员登陆接口
		UserLoginInpDTO userLoginInpDTO = MeiteBeanUtils.voToDto(loginVo, UserLoginInpDTO.class);
		//设置登录的来源
		userLoginInpDTO.setLoginType(MemberConstants.MEMBER_LOGIN_TYPE_PC);
		String info = webBrowserInfo(request);
		userLoginInpDTO.setDeviceInfor(info);

		BaseResponse<JSONObject> login = memberLoginServiceFeign.login(userLoginInpDTO);
		if(!isSuccess(login)){
			setErrorMsg(model,login.getMsg());
			return MB_LOGIN_FTL;
		}
		//登录成功后, 保持会话信息 将token存到cookie里面 首先读取cookietoken 查询用户信息
		JSONObject data = login.getData();
		String token = data.getString("token");
		CookieUtils.setCookie(request,response, WebConstants.LOGIN_TOKEN_COOKIENAME,token);
		return REDIRECT_INDEX;
	}

}
