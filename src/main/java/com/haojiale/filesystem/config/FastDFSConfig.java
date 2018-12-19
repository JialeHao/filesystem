package com.haojiale.filesystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author haojiale
 * @Date 2018/12/19 11:02
 * @Version 1.0
 **/
public class FastDFSConfig {
    /**
     * 路径分隔符
     */
    public static final String SEPARATOR = "/";
    /**
     * Point
     */
    public static final String POINT = ".";
    /**
     * ContentType
     */
    public static final Map<String, String> EXT_MAPS = new HashMap<>();

    /**
     * org.slf4j.Logger
     */
    private static Logger logger = LoggerFactory.getLogger(FastDFSConfig.class);
    /**
     * 文件名称Key
     */
    public static final String FILENAME = "filename";


    public FastDFSConfig() {
        initExt();
    }

    private void initExt() {
        // image
        EXT_MAPS.put("png", "image/png");
        EXT_MAPS.put("gif", "image/gif");
        EXT_MAPS.put("bmp", "image/bmp");
        EXT_MAPS.put("ico", "image/x-ico");
        EXT_MAPS.put("jpeg", "image/jpeg");
        EXT_MAPS.put("jpg", "image/jpeg");
        // 压缩文件
        EXT_MAPS.put("zip", "application/zip");
        EXT_MAPS.put("rar", "application/x-rar");
        // doc
        EXT_MAPS.put("pdf", "application/pdf");
        EXT_MAPS.put("ppt", "application/vnd.ms-powerpoint");
        EXT_MAPS.put("xls", "application/vnd.ms-excel");
        EXT_MAPS.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        EXT_MAPS.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        EXT_MAPS.put("doc", "application/msword");
        EXT_MAPS.put("doc", "application/wps-office.doc");
        EXT_MAPS.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        EXT_MAPS.put("txt", "text/plain");
        // 音频
        EXT_MAPS.put("mp4", "video/mp4");
        EXT_MAPS.put("flv", "video/x-flv");
    }
}
