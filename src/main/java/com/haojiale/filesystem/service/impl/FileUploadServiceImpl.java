package com.haojiale.filesystem.service.impl;

import com.haojiale.filesystem.config.FastDFSConfig;
import com.haojiale.filesystem.config.TrackerServerPool;
import com.haojiale.filesystem.enums.BusinessCode;
import com.haojiale.filesystem.exception.FileSystemException;
import com.haojiale.filesystem.service.FileUploadService;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerServer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author haojiale
 * @Date 2018/12/19 11:45
 * @Version 1.0
 **/
@Service
public class FileUploadServiceImpl implements FileUploadService {
    /**
     * 文件最大的大小
     */
    private int maxFileSize = 100 * 1000 * 1000;

    /**
     * 使用 MultipartFile 上传
     * @param file MultipartFile
     * @param descriptions 文件描述信息
     * @return 文件路径
     */
    @Override
    public String upload(MultipartFile file, Map<String, String> descriptions) throws FileSystemException {
        if (file == null || file.isEmpty()) {
            throw new FileSystemException(BusinessCode.FILE_IS_NULL);
        }
        String path = null;
        try {
            path = upload(file.getInputStream(), file.getOriginalFilename(), descriptions);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileSystemException(BusinessCode.FILE_IS_NULL);
        }
        return path;
    }

    /**
     * 以附件形式下载文件 可以指定文件名称.
     * @param filepath 文件路径
     * @param filename 文件名称
     * @param response HttpServletResponse
     */
    @Override
    public void download(String filepath, String filename, HttpServletResponse response) {
        try {
            download(filepath, null, response.getOutputStream(), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 上传通用方法
     * @param is 文件输入流
     * @param filename 文件名
     * @param descriptions 文件描述信息
     * @return 组名+文件路径，如：group1/M00/00/00/wKgz6lnduTeAMdrcAAEoRmXZPp870.jpeg
     */
    public String upload(InputStream is, String filename, Map<String, String> descriptions) {
        if (null == is) {
            throw new FileSystemException(BusinessCode.FILE_IS_NULL);
        }

        try {
            if (is.available() > maxFileSize) {
                throw new FileSystemException(BusinessCode.FILE_OUT_SIZE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        filename = toLocal(filename);
        // 返回路径
        String path = null;
        // 文件描述
        NameValuePair[] nvps = null;
        List<NameValuePair> nvpsList = new ArrayList<>();
        // 文件名后缀
        String suffix = getFilenameSuffix(filename);

        // 文件名
        if (StringUtils.isNotBlank(filename)) {
            nvpsList.add(new NameValuePair(FastDFSConfig.FILENAME, filename));
        }
        // 描述信息
        if (descriptions != null && descriptions.size() > 0) {
            descriptions.forEach((key, value) -> {
                nvpsList.add(new NameValuePair(key, value));
            });
        }
        if (nvpsList.size() > 0) {
            nvps = new NameValuePair[nvpsList.size()];
            nvpsList.toArray(nvps);
        }

        TrackerServer trackerServer = TrackerServerPool.borrowObject();
        StorageClient1 storageClient = new StorageClient1(trackerServer, null);
        try {
            // 读取流
            byte[] fileBuff = new byte[is.available()];
            is.read(fileBuff, 0, fileBuff.length);

            // 上传
            path = storageClient.upload_file1(fileBuff, suffix, nvps);

            if (StringUtils.isBlank(path)) {
                throw new FileSystemException(BusinessCode.FILE_UPLOAD_FAILED);
            }

            /*if (logger.isDebugEnabled()) {
                logger.debug("upload file success, return path is {}", path);
            }*/
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileSystemException(BusinessCode.FILE_UPLOAD_FAILED);
        } catch (MyException e) {
            e.printStackTrace();
            throw new FileSystemException(BusinessCode.FILE_UPLOAD_FAILED);
        } finally {
            // 关闭流
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 返还对象
        TrackerServerPool.returnObject(trackerServer);

        return path;
    }
    /**
     * 下载文件
     * @param filepath 文件路径
     * @param filename 文件名称
     * @param os 输出流
     * @param response HttpServletResponse
     */
    public void download(String filepath, String filename, OutputStream os, HttpServletResponse response) throws FileSystemException {
        if (StringUtils.isBlank(filepath)) {
            throw new FileSystemException(BusinessCode.FILE_PATH_IS_NULL);
        }

        filepath = toLocal(filepath);
        // 文件名
        if (StringUtils.isBlank(filename)) {
            filename = getOriginalFilename(filepath);
        }
        String contentType = FastDFSConfig.EXT_MAPS.get(getFilenameSuffix(filename));

        /*if (logger.isDebugEnabled()) {
            logger.debug("download file, filepath = {}, filename = {}", filepath, filename);
        }*/

        TrackerServer trackerServer = TrackerServerPool.borrowObject();
        StorageClient1 storageClient = new StorageClient1(trackerServer, null);
        InputStream is = null;
        try {
            // 下载
            byte[] fileByte = storageClient.download_file1(filepath);

            if (fileByte == null) {
                throw new FileSystemException(BusinessCode.FILE_NOT_EXIST);
            }

            if (response != null) {
                os = response.getOutputStream();

                // 设置响应头
                if (StringUtils.isNotBlank(contentType)) {
                    // 文件编码 处理文件名中的 '+'、' ' 特殊字符
                    String encoderName = URLEncoder.encode(filename, "UTF-8").replace("+", "%20").replace("%2B", "+");
                    response.setHeader("Content-Disposition", "attachment;filename=\"" + encoderName + "\"");
                    response.setContentType(contentType + ";charset=UTF-8");
                    response.setHeader("Accept-Ranges", "bytes");
                }
            }

            is = new ByteArrayInputStream(fileByte);
            byte[] buffer = new byte[1024 * 5];
            int len = 0;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
            throw new FileSystemException(BusinessCode.FILE_DOWNLOAD_FAILED);
        } finally {
            // 关闭流
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 返还对象
        TrackerServerPool.returnObject(trackerServer);
    }

    /**
     * 转换路径中的 '\' 为 '/' <br>
     * 并把文件后缀转为小写
     * @param path 路径
     * @return
     */
    public static String toLocal(String path) {
        if (StringUtils.isNotBlank(path)) {
            path = path.replaceAll("\\\\", FastDFSConfig.SEPARATOR);

            if (path.contains(FastDFSConfig.POINT)) {
                String pre = path.substring(0, path.lastIndexOf(FastDFSConfig.POINT) + 1);
                String suffix = path.substring(path.lastIndexOf(FastDFSConfig.POINT) + 1).toLowerCase();
                path = pre + suffix;
            }
        }
        return path;
    }

    /**
     * 获取文件名称的后缀
     * @param filename 文件名 或 文件路径
     * @return 文件后缀
     */
    public static String getFilenameSuffix(String filename) {
        String suffix = null;
        String originalFilename = filename;
        if (StringUtils.isNotBlank(filename)) {
            if (filename.contains(FastDFSConfig.SEPARATOR)) {
                filename = filename.substring(filename.lastIndexOf(FastDFSConfig.SEPARATOR) + 1);
            }
            if (filename.contains(FastDFSConfig.POINT)) {
                suffix = filename.substring(filename.lastIndexOf(FastDFSConfig.POINT) + 1);
            } else {
                /*if (logger.isErrorEnabled()) {
                    logger.error("filename error without suffix : {}", originalFilename);
                }*/
            }
        }
        return suffix;
    }
    /**
     * 获取源文件的文件名称
     *
     * @param filepath 文件路径
     * @return 文件名称
     */
    public String getOriginalFilename(String filepath) throws FileSystemException {
        Map<String, Object> descriptions = getFileDescriptions(filepath);
        if (descriptions.get(FastDFSConfig.FILENAME) != null) {
            return (String) descriptions.get(FastDFSConfig.FILENAME);
        }
        return null;
    }
    /**
     * 获取文件描述信息
     *
     * @param filepath 文件路径
     * @return 文件描述信息
     */
    public Map<String, Object> getFileDescriptions(String filepath) throws FileSystemException {
        TrackerServer trackerServer = TrackerServerPool.borrowObject();
        StorageClient1 storageClient = new StorageClient1(trackerServer, null);
        NameValuePair[] nvps = null;
        try {
            nvps = storageClient.get_metadata1(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        // 返还对象
        TrackerServerPool.returnObject(trackerServer);

        Map<String, Object> infoMap = null;

        if (nvps != null && nvps.length > 0) {
            infoMap = new HashMap<>(nvps.length);

            for (NameValuePair nvp : nvps) {
                infoMap.put(nvp.getName(), nvp.getValue());
            }
        }

        return infoMap;
    }
}
