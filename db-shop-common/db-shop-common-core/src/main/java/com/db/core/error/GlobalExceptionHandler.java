package com.db.core.error;

import com.alibaba.fastjson.JSONObject;
import com.db.base.BaseApiService;
import com.db.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局捕获异常
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends BaseApiService<JSONObject> {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public BaseResponse<JSONObject> exceptionHandler(Exception e) {
        log.info("###全局捕获异常###,error:{}", e);
        return setResultError("系统错误!");
    }

}
