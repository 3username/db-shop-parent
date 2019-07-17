package com.db.controller;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseResponse;
import com.db.controller.req.vo.RegisterVo;
import com.db.feign.MemberRegisterServiceFeign;
import com.db.web.base.BaseWebController;
import com.db.web.bean.MeiteBeanUtils;
import com.db.web.utils.RandomValidateCodeUtil;
import com.mayikt.member.input.dto.UserInpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class RegisterController extends BaseWebController {
	/**注册页面*/
	private static final String MB_REGISTER_FTL = "member/register";
	/**跳转到登陆页面*/
	private static final String MB_LOGIN_FTL = "member/login";

	private final MemberRegisterServiceFeign memberRegisterServiceFeign;
    @Autowired
	public RegisterController(MemberRegisterServiceFeign memberRegisterServiceFeign) {
		this.memberRegisterServiceFeign = memberRegisterServiceFeign;
	}

	/**
	 * 跳转到注册页面
	 * @return
	 */
	@GetMapping("/register.html")
	public String getRegister() {
		return MB_REGISTER_FTL;
	}

	/**
	 * 跳转到注册页面
	 * 
	 * @return
	 */
	@PostMapping("/register")
	public String postRegister(RegisterVo registerVo, BindingResult
			bindingResult, Model model, HttpSession httpSession) {
		//接收表单参数,创建对象接收参数vo 判断参数
		if(bindingResult.hasErrors()){
			//如果参数有错误 获取第一个
			String errorMsg = bindingResult.getFieldError().getDefaultMessage();
			setErrorMsg(model,errorMsg);
			return MB_REGISTER_FTL;
		}
		//判断图形验证码是否正确
		String graphicCode = registerVo.getGraphicCode();
		Boolean checkVerify = RandomValidateCodeUtil.checkVerify(graphicCode, httpSession);
		if(!checkVerify){
			setErrorMsg(model,"图形码验证不正确");
			return MB_REGISTER_FTL;
		}
		//调用会员注册接口注册, vo转换dto
		UserInpDTO userInpDTO = MeiteBeanUtils.voToDto(registerVo, UserInpDTO.class);
		BaseResponse<JSONObject> register = memberRegisterServiceFeign.register(userInpDTO, registerVo.getRegistCode());
		if(!isSuccess(register)){
			setErrorMsg(model,register.getMsg());
			return MB_REGISTER_FTL;
		}
		// 跳转到登陆页面
		return MB_LOGIN_FTL;
	}

}
