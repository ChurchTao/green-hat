package com.greenhat.mvc;


import com.greenhat.mvc.bean.FormParam;
import com.greenhat.mvc.bean.Param;
import com.greenhat.util.ArrayUtil;
import com.greenhat.util.CodecUtil;
import com.greenhat.util.StreamUtil;
import com.greenhat.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by jiacheng on 2017/8/2.
 */
public class RequestHelper {
    public static Param createParam(HttpServletRequest request) throws IOException{
        List<FormParam> formParamList = new ArrayList<FormParam>();
        formParamList.addAll(parseParameterNames(request));
        formParamList.addAll(parseInputStream(request));
        return new Param(formParamList);
    }

    private static List<FormParam> parseParameterNames(HttpServletRequest request){
        List<FormParam> formParamList = new ArrayList<FormParam>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()){
            String fieldName = paramNames.nextElement();
            String[] fieldValues = request.getParameterValues(fieldName);
            if (ArrayUtil.isNotEmpty(fieldValues)){
                Object fieldValue;
                if (fieldValues.length==1){
                    fieldValue = fieldValues[0];
                }else{
                    StringBuilder builder = new StringBuilder("");
                    for (int i=0;i<fieldValues.length;i++){
                        if (i!=fieldValues.length-1){
                            builder.append(StringUtil.SEPARATOR);
                        }
                    }
                    fieldValue = builder.toString();
                }
                formParamList.add(new FormParam(fieldName,fieldValue));

            }
        }
        return formParamList;
    }
    private static List<FormParam> parseInputStream(HttpServletRequest request) throws  IOException{
        List<FormParam> formParamList = new ArrayList<FormParam>();
        String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if (StringUtil.isNotEmpty(body)){
            String[] key_val_s = StringUtil.splitString(body,"&");
            if (ArrayUtil.isNotEmpty(key_val_s)){
                for (String key_val:key_val_s){
                    String[] array = StringUtil.splitString(key_val,"=");
                    if (ArrayUtil.isNotEmpty(array)&&array.length==2){
                        String fieldName = array[0];
                        String fieldValue=array[1];
                        formParamList.add(new FormParam(fieldName,fieldValue));
                    }
                }
            }
        }
        return formParamList;
    }
}
