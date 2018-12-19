package com.haojiale.filesystem.service;

import com.haojiale.filesystem.model.JsonResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description TODO
 * @Author haojiale
 * @Date 2018/12/17 14:43
 * @Version 1.0
 **/
public interface FileSystemService {

    JsonResult uploadFile(MultipartFile file, String businessTag, Long businessId, String metaData);

    JsonResult downloadFile(String path, HttpServletResponse response);
}
