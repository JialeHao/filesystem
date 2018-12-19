package com.haojiale.filesystem.enums;

public enum BusinessCode {
    FILE_IS_NULL("5001","文件为空"),
    FILE_OUT_SIZE("5002","文件超出大小限制"),
    FILE_UPLOAD_FAILED("5003","文件上传失败"),
    SUCCESS("5004", "成功"),
    FILE_PATH_IS_NULL("5005","文件路径为空"),
    FILE_DOWNLOAD_FAILED("5006", "文件下载失败"),
    FILE_NOT_EXIST("5007", "文件不存在");
    private String code;
    private String msg;

    BusinessCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
