package com.greenhat.mvc.request;

import com.greenhat.json.JSONRequestBean;
import com.greenhat.mvc.fault.ServerException;
import com.greenhat.util.JsonUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class JsonReader {
    public static Map receiveJson(HttpServletRequest request) throws IOException{

        // 读取请求内容
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        //将json字符串转换为json对象
        return JsonUtil.fromJSON(sb.toString(),Map.class);
    }

    public static String getJsonStr(HttpServletRequest request) throws IOException {
        // 读取请求内容
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String temp = sb.toString();
        if (temp.equals("")){
            temp="[]";
        }else {
            if (!temp.startsWith("[")){
                temp = "["+temp;
            }
            if (!temp.endsWith("]")){
                temp = temp+"]";
            }
        }
        return temp;
    }


    public static JSONRequestBean readRequest(HttpServletRequest request) throws IOException,ServerException{
        String service = request.getHeader(JsonRequestEnum.SERVICE.toString());
        String method = request.getHeader(JsonRequestEnum.METHOD.toString());
        // 读取请求内容
        List list = JsonUtil.fromJSON(getJsonStr(request), ArrayList.class);
        int size = list.size();
        if (size==0){
            return new JSONRequestBean(service,method,new Object[]{});
        }
        if (size==1){
            Object o = list.get(0);
            if (o instanceof Map) {
                Map properties = (Map) o;
                return new JSONRequestBean(service, method, new Object[]{o}, properties);
            }
            return new JSONRequestBean(service,method,new Object[]{o});
        }
        return new JSONRequestBean(service,method, list.toArray());

    }
}
