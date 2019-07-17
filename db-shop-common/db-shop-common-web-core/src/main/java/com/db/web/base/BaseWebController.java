package com.db.web.base;

import com.db.base.BaseResponse;
import com.db.constants.Constants;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.UserAgent;
import nl.bitwalker.useragentutils.Version;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

public class BaseWebController {
    /**系统异常*/
    protected final static String ERROR_500_FTL = "500";

    /**接口直接返回true 或者false*/
    public Boolean isSuccess(BaseResponse<?> baseResponse){
        if(baseResponse == null){
            return false;
        }
        if(!baseResponse.getCode().equals(Constants.HTTP_RES_CODE_200)){
            return false;
        }
        return true;
    }

    /**获取浏览器信息*/
    public String webBrowserInfo(HttpServletRequest request){
        String userAgentString = request.getHeader("User-Agent");
        //获取浏览器信息
        Browser browser = UserAgent.parseUserAgentString(userAgentString).getBrowser();
        //获取浏览器版本号
        Version version = browser.getVersion(userAgentString);
        String info = browser.getName() + "/" + version.getVersion();
        return info;
    }

    public void setErrorMsg(Model model, String errorMsg){
        model.addAttribute("error",errorMsg);
    }


}
