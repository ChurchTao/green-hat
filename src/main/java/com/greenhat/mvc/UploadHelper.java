package com.greenhat.mvc;

import com.greenhat.ConfigNames;
import com.greenhat.mvc.bean.FileParam;
import com.greenhat.mvc.bean.FormParam;
import com.greenhat.mvc.bean.Param;
import com.greenhat.util.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UploadHelper {

    private static final Logger logger = LoggerFactory.getLogger(UploadHelper.class);

    /**
     * FileUpload 对象（用于解析所上传的文件）
     */
    private static ServletFileUpload fileUpload;

    /**
     * 初始化
     */
    public static void init(ServletContext servletContext) {
        // 获取一个临时目录（使用 Tomcat 的 work 目录）
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        // 创建 FileUpload 对象
        fileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));
        // 设置上传限制
        int uploadLimit = ConfigNames.UPLOAD_LIMIT;
        if (uploadLimit != 0) {
            fileUpload.setFileSizeMax(uploadLimit * 1024 * 1024); // 单位为 M
        }
    }

    /**
     * 判断请求是否为 multipart 类型
     */
    public static boolean isMultipart(HttpServletRequest request) {
        // 判断上传文件的内容是否为 multipart 类型
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * 创建 multipart 请求参数列表
     */
    public static Param createParam(HttpServletRequest request) throws Exception {
        // 定义参数列表
        List<FormParam> formParamList = new ArrayList<FormParam>();
        List<FileParam> fileParamList = new ArrayList<FileParam>();

        try {
           Map<String,List<FileItem>> fileItemListMap = fileUpload.parseParameterMap(request);
           if (MapUtil.isNotEmpty(fileItemListMap)){
                for (Map.Entry<String,List<FileItem>> fileItemListEntry:fileItemListMap.entrySet()){
                    String fieldName = fileItemListEntry.getKey();
                    List<FileItem> fileItemList = fileItemListEntry.getValue();
                    if (CollectionUtil.isNotEmpty(fileItemList)){
                        for (FileItem fileItem : fileItemList){
                            if (fileItem.isFormField()){
                                String fieldValue = fileItem.getString("UTF-8");
                                formParamList.add(new FormParam(fieldName,fieldValue));
                            }else {
                                String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes("utf-8"),"utf-8"));
                                if (StringUtil.isNotEmpty(fileName)){
                                    long fileSize = fileItem.getSize();
                                    String contentType = fileItem.getContentType();
                                    InputStream inputStream = fileItem.getInputStream();
                                    fileParamList.add(new FileParam(fieldName,fileName,fileSize,contentType,inputStream));
                                }
                            }
                        }
                    }
                }
           }
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            // 异常转换（抛出自定义异常）
            throw new RuntimeException(e);
        }
        return new Param(formParamList,fileParamList);

    }

    /**
     * 上传文件
     */
    public static void uploadFile(String basePath, FileParam fileParam) throws IOException {
        FileOutputStream fileOutputStream =null;
        try {
            if (fileParam != null) {
                // 创建文件路径（绝对路径）
                String filePath = basePath + fileParam.getFileName();
                FileUtil.createFile(filePath);
                // 执行流复制操作
                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
                fileOutputStream =new FileOutputStream(filePath);
                OutputStream outputStream = new BufferedOutputStream(fileOutputStream);
                StreamUtil.copyStream(inputStream, outputStream);
            }
        } catch (Exception e) {
            logger.error("上传文件出错！", e);
            throw new RuntimeException(e);
        } finally {
            fileOutputStream.close();
        }
    }

    /**
     * 批量上传文件
     */
    public static void uploadFiles(String basePath, List<FileParam> fileParamList) {
        try{
            if (CollectionUtil.isNotEmpty(fileParamList)){
                for (FileParam fileParam:fileParamList){
                    uploadFile(basePath,fileParam);
                }
            }
        }catch (Exception e){
            throw new RuntimeException("upload file error");
        }
    }
}
