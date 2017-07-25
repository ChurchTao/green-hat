package com.greenhat.loader;



import com.greenhat.annotation.Mapping;
import com.greenhat.mvc.bean.Handler;
import com.greenhat.mvc.bean.Request;
import com.greenhat.util.ArrayUtil;
import com.greenhat.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jiacheng on 2017/7/19.
 */
public final class ControllerLoader {
    private static final Map<Request,Handler> actionMap = new HashMap<Request, Handler>();

    static {
        List<Class<?>> controllerList = ClassLoader.getControllerClasses();
        if (CollectionUtil.isNotEmpty(controllerList)){
            for (Class<?> controllerClass:controllerList){
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)){
                    for (Method method:methods){
                        if (method.isAnnotationPresent(Mapping.class)){
                            Mapping action = method.getAnnotation(Mapping.class);
                            String mapping = action.value();
                            if (mapping.matches("\\w+:/\\w*")){
                                String[] array =mapping.split(":");
                                if (ArrayUtil.isNotEmpty(array)&& array.length==2){
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod,requestPath);
                                    Handler handler = new Handler(controllerClass,method);
                                    actionMap.put(request,handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(String method,String path){

        Handler handler = null;
        for (Map.Entry<Request, Handler> actionEntry : actionMap.entrySet()) {
            // 从 Requester 中获取 Request 相关属性
            Request requester = actionEntry.getKey();
            String requestMethod = requester.getRequestMethod();
            String requestPath = requester.getRequestPath(); // 正则表达式
            // 获取请求路径匹配器（使用正则表达式匹配请求路径并从中获取相应的请求参数）
            Matcher requestPathMatcher = Pattern.compile(requestPath).matcher(path);
            // 判断请求方法与请求路径是否同时匹配
            if (requestMethod.equalsIgnoreCase(method) && requestPathMatcher.matches()) {
                // 获取 Handler 及其相关属性
                handler = actionEntry.getValue();
                break;
            }
        }
            return handler;
    }
}
