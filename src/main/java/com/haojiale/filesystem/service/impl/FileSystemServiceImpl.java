package com.haojiale.filesystem.service.impl;

import com.haojiale.filesystem.enums.BusinessCode;
import com.haojiale.filesystem.model.JsonResult;
import com.haojiale.filesystem.service.FileSystemService;
import com.haojiale.filesystem.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description TODO
 * @Author haojiale
 * @Date 2018/12/17 15:20
 * @Version 1.0
 **/
@Service
public class FileSystemServiceImpl implements FileSystemService {

    @Autowired
    private FileUploadService fileUploadService;


    @Override
    public JsonResult uploadFile(MultipartFile file, String businessTag, Long businessId, String metaData) {
        String path = fileUploadService.upload(file, null);
        //TODO 入库操作
        return JsonResult.buildResponse(BusinessCode.SUCCESS,path);
    }

    @Override
    public JsonResult downloadFile(String path, HttpServletResponse response) {
        //TODO 去库中校验
        fileUploadService.download(path,null,response);

        return JsonResult.buildResponse(BusinessCode.SUCCESS);
    }

}
