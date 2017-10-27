package com.greenhat.mvc.request;

import com.greenhat.util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
}
