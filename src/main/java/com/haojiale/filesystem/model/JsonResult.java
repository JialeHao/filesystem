package com.haojiale.filesystem.model;

import com.haojiale.filesystem.enums.BusinessCode;
import lombok.Data;

/**
 * @Description TODO
 * @Author haojiale
 * @Date 2018/12/17 14:41
 * @Version 1.0
 **/
@Data
public class JsonResult<T> {
    private String code;
    private String msg;
    private T data;

    public JsonResult() {
    }

    public JsonResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static JsonResult buildResponse(String code, String msg){
        return new JsonResult(code,msg,null);

    }
    public static JsonResult buildResponse(BusinessCode businessCode){
        return new JsonResult(businessCode.getCode(),businessCode.getMsg(),null);

    }
    public static JsonResult buildResponse(BusinessCode businessCode,Object data){
        return new JsonResult(businessCode.getCode(),businessCode.getMsg(),data);

    }
}
