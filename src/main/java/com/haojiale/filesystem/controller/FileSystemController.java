package com.haojiale.filesystem.controller;

import com.haojiale.filesystem.model.JsonResult;
import com.haojiale.filesystem.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description TODO
 * @Author haojiale
 * @Date 2018/12/17 14:41
 * @Version 1.0
 **/
@RestController
public class FileSystemController {

    @Autowired
    private FileSystemService fileSystemService;

    @RequestMapping(value = "/uploadFile")
    public JsonResult uploadFile(MultipartFile file, String businessTag, Long businessId, String metaData){
        JsonResult jsonResult = fileSystemService.uploadFile(file,businessTag,businessId,metaData);
        return jsonResult;
    }

    @RequestMapping(value = "/downloadFile")
    public JsonResult downloadFile(String path,HttpServletResponse response){
        JsonResult jsonResult = fileSystemService.downloadFile(path,response);
        return jsonResult;
    }
}
