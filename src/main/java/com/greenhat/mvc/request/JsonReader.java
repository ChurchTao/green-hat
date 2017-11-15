package com.greenhat.mvc.request;

import com.greenhat.json.JSONRequestBean;
import com.greenhat.mvc.fault.ServerException;
import com.greenhat.util.JsonUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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


    public static JSONRequestBean readRequest(HttpServletRequest request) throws IOException,ServerException{
        String service = request.getHeader(JsonRequestEnum.SERVICE.toString());
        String method = request.getHeader(JsonRequestEnum.METHOD.toString());
        // 读取请求内容
        Map map = receiveJson(request);
        Object body = map.get(JsonRequestEnum.BODY.toString());
        if (body instanceof List){
            return new JSONRequestBean(service,method, ((List) body).toArray());
        }else if (body instanceof Map) {
            Map  properties = (Map) body;
            return new JSONRequestBean(service,method,new Object[]{body}, properties);
        }else {
            throw new ServerException(500,"Json 解析异常，请检查json格式");
        }

    }
}
