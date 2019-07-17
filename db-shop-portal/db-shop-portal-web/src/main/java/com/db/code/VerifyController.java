package com.db.code;

import com.db.web.utils.RandomValidateCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class VerifyController {

    @GetMapping("/getVerify")
    public void getVerify(HttpServletRequest request,
                          HttpServletResponse response){
        try {
            // 设置相应类型,告诉浏览器输出的内容为图片
            response.setContentType("image/jpeg");
            // 设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control","No-Cache");
            response.setDateHeader("Expire",0);
            RandomValidateCodeUtil randomValidateCodeUtil = new RandomValidateCodeUtil();
            randomValidateCodeUtil.getRandcode(request,response);
        }catch (Exception e)
        {}
    }
}
