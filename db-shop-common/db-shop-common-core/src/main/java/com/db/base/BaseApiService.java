package com.db.base;

import com.db.constants.Constants;
import lombok.Data;
import org.springframework.stereotype.Component;

/***
 * 微服务接口实现该接口可以使用传递参数可以直接封装统一返回结果集
 * @param <T>
 */
@Data
@Component
public class BaseApiService<T> {

    /**返回错误，*/
    public BaseResponse setResultError(Integer code,String msg){
        return setResult(code,msg,null);
    }
    /**返回错误，传code传msg*/
    public BaseResponse setResultError(String msg,T data){
        return setResult(Constants.HTTP_RES_CODE_500,msg,data);
    }

    /**返回错误，只传msg*/
    public BaseResponse setResultError(String msg){
        return setResult(Constants.HTTP_RES_CODE_500,msg,null);
    }

    /**返回成功，可以传data值*/
    public BaseResponse setResultSuccess(T data){
        return setResult(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE,data);
    }

    /**返回成功，沒有data值*/
    public BaseResponse setResultSuccess(){
        return setResult(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE,null);
    }

    /**返回成功，只传msg值*/
    public BaseResponse setResultSuccess(String msg){
        return setResult(Constants.HTTP_RES_CODE_200, msg,null);
    }

    /*通用封装*/
    public BaseResponse<T> setResult(Integer code, String msg, T data) {
        return new BaseResponse<T>(code, msg, data);
    }
}
