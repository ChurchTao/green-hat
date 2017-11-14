package com.greenhat.mvc.bean;


import com.greenhat.util.CastUtil;
import com.greenhat.util.CollectionUtil;
import com.greenhat.util.MapUtil;
import com.greenhat.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiacheng on 2017/7/19.
 */
public class Param {
    private List<FormParam> formParamList;

    private List<FileParam> fileParamList;

    private Map<String,Object> paramMap;

    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 获取表单的请求参数-映射
     * @return
     */
    public Map<String,Object> getFieldMap(){
        Map<String,Object> fieldMap = new HashMap<String, Object>();
        if (CollectionUtil.isNotEmpty(formParamList)){
            for (FormParam formParam:formParamList) {
                String fieldName = formParam.getFieldName();
                Object fieldValue = formParam.getFieldValue();
                if (fieldMap.containsKey(fieldName)){
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR+fieldValue;
                }
                fieldMap.put(fieldName,fieldValue);
            }
        }
        return fieldMap;
    }

    /**
     * 获取文件上传的映射
     * @return
     */
    public Map<String,List<FileParam>> getFileMap(){
        Map<String,List<FileParam>> fileMap = new HashMap<String, List<FileParam>>();
        if (CollectionUtil.isNotEmpty(fileParamList)){
            for (FileParam fileParam:fileParamList){
                String fieldName = fileParam.getFieldName();
                List<FileParam> fileParamList;
                if (fileMap.containsKey(fieldName)){
                    fileParamList=fileMap.get(fieldName);
                }else {
                    fileParamList=new ArrayList<FileParam>();
                }
                fileParamList.add(fileParam);
                fileMap.put(fieldName,fileParamList);
            }
        }
        return fileMap;
    }

    /**
     * 获取所有上传文件
     * @param fieldName
     * @return
     */
    public List<FileParam> getFileList(String fieldName){
        return getFileMap().get(fieldName);
    }

    public FileParam getFile(String fieldName){
        List<FileParam> fileParamList = getFileList(fieldName);
        if (CollectionUtil.isNotEmpty(fileParamList)&&fileParamList.size()==1){
            return fileParamList.get(0);
        }
        return null;
    }
    public boolean isEmpty(){
        return CollectionUtil.isEmpty(formParamList) && CollectionUtil.isEmpty(fileParamList);
    }
    public String getString(String name) {
        return CastUtil.castString(get(name));
    }

    public double getDouble(String name) {
        return CastUtil.castDouble(get(name));
    }

    public long getLong(String name) {
        return CastUtil.castLong(get(name));
    }

    public int getInt(String name) {
        return CastUtil.castInt(get(name));
    }

    public Map<String,Object> getJsonBody(){
        return (Map<String, Object>) paramMap.get("Body");
    }

    public Object get(String name) {
        return paramMap.get(name);
    }

    public Map<String,Object> getMap(){
        return paramMap;
    }


}
