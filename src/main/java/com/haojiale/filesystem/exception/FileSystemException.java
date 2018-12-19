package com.haojiale.filesystem.exception;

import com.haojiale.filesystem.enums.BusinessCode;

/**
 * @Description 自定义异常处理
 * @Author haojiale
 * @Date 2018/12/19 10:56
 * @Version 1.0
 **/
public class FileSystemException extends RuntimeException{
    private String code;
    private String msg;

    public FileSystemException(String code,String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
    public FileSystemException(BusinessCode businessCode) {
        super(businessCode.getMsg());
        this.code = businessCode.getCode();
        this.msg = businessCode.getMsg();
    }
}
