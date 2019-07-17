package com.db.controller;

import com.db.base.BaseResponse;
import com.db.constants.WebConstants;
import com.db.feign.MemberServiceFeign;
import com.db.web.base.BaseWebController;
import com.db.web.utils.CookieUtils;
import com.mayikt.member.output.dto.UserOutDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 首页
 */
@Controller
public class IndexController extends BaseWebController {
	/**首页*/
	private final static String INDEX_FTL = "index";

	private final MemberServiceFeign memberServiceFeign;
	@Autowired
	public IndexController(MemberServiceFeign memberServiceFeign) {
		this.memberServiceFeign = memberServiceFeign;
	}

	/**
	 * 跳转到首页
	 */
	@GetMapping("/")
	public String index(HttpServletRequest request, Model model,
						HttpServletResponse response) {
		//从cookie中获取 会员token
		String token = CookieUtils.getCookieValue(request, WebConstants.LOGIN_TOKEN_COOKIENAME, true);
		if(!StringUtils.isBlank(token)){
			//调用会员服务接口,查询会员用户信息
			BaseResponse<UserOutDTO> userInfo = memberServiceFeign.getInfo(token);
			if(isSuccess(userInfo)){
				UserOutDTO data = userInfo.getData();
				if(data != null){
					String mobile = data.getMobile();
					//对号码脱敏
					String desensMobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
					model.addAttribute("desensMobile",desensMobile);
				}
			}
		}
		return INDEX_FTL;
	}


	/**
	 * 跳转到首页
	 * 
	 * @return
	 */
	@RequestMapping("/index.html")
	public String indexHtml() {
		return INDEX_FTL;
	}
}
