package com.haojiale.filesystem.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description TODO
 * @Author haojiale
 * @Date 2018/12/19 11:45
 * @Version 1.0
 **/
public interface FileUploadService {

    String upload(MultipartFile file, Map<String, String> descriptions);

    void download(String filepath, String filename, HttpServletResponse response);
}
