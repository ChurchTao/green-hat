package com.greenhat.loader;


import com.greenhat.ioc.annotation.Mapping;
import com.greenhat.mvc.bean.Handler;
import com.greenhat.mvc.bean.Request;
import com.greenhat.mvc.fault.ServerException;
import com.greenhat.mvc.request.JsonReader;
import com.greenhat.util.ArrayUtil;
import com.greenhat.util.CollectionUtil;
import com.greenhat.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by jiacheng on 2017/7/19.
 */
public final class ControllerLoader {
    private static final Logger logger = LoggerFactory.getLogger(ControllerLoader.class);

    private static final Map<Request, Handler> actionMap = new ConcurrentHashMap<Request, Handler>();
    private static final Map<Class<?>, Map<String,Method>> serviceMap = new ConcurrentHashMap<Class<?>, Map<String,Method>>();

    static {
        logger.info("ControllerLoader init start!");
        List<Class<?>> controllerList = ClassLoader.getControllerClasses();
        if (CollectionUtil.isNotEmpty(controllerList)) {
            for (Class<?> controllerClass : controllerList) {
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Mapping.class)) {
                            if (serviceMap.containsKey(controllerClass)){
                                Map<String,Method> services = serviceMap.get(controllerClass);
                                services.put(method.getName(),method);
                            }else {
                                Map<String,Method> services = new HashMap<>();
                                services.put(method.getName(),method);
                                serviceMap.put(controllerClass,services);
                            }
                            Mapping action = method.getAnnotation(Mapping.class);
                            String mapping = action.value();
                            String requestMethod = action.method().toString();
                            if (!mapping.startsWith("/")) {
                                StringBuilder builder = new StringBuilder(mapping);
                                builder.insert(0, "/");
                                mapping = builder.toString();
                            }
                            if (mapping.matches("/[./\\w_]+")) {
                                Request request = new Request(requestMethod, mapping);
                                Handler handler = new Handler(controllerClass, method);
                                actionMap.put(request, handler);
                                logger.info("Put [{}] into [{}] Action Mapping~", mapping, controllerClass.getName());
                            }
                        }
                    }
                }
                logger.info("Total [{}] Service method in [{}]  ~", serviceMap.get(controllerClass).size(), controllerClass.getName());
            }
        }
    }

    public static Handler getHandler(String method, String path, HttpServletRequest request) throws Exception {

        Handler handler = null;
        if (path.equals("/*JsonRequest")){
            String serviceName = request.getHeader("Service");
            String methodName = request.getHeader("Method");
//            Map map = JsonReader.receiveJson(request);
//            if (checkJsonRequest(map)){
            boolean checkJson =(StringUtil.isEmpty(serviceName)||StringUtil.isNotEmpty(methodName));
            if (!checkJson){
                for (Map.Entry<Class<?>, Map<String,Method>> serviceEntry : serviceMap.entrySet()){
                    Class<?> controller = serviceEntry.getKey();
                    Map<String,Method> methodMap= serviceEntry.getValue();
                    if (controller.getName().equalsIgnoreCase(serviceName)&&methodMap.containsKey(methodName)){
                        handler = new Handler(controller,methodMap.get(methodName));
                        return handler;
                    }
                }
                return null;
            }else {
                throw new ServerException(ServerException.VALUE_NEEDED,"Json 请求格式不正确!");
            }
        }
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
//
//    private static boolean checkJsonRequest(Map map){
//        return (map.containsKey("Service")&& map.containsKey("Method")&& map.containsKey("Body"));
//    }
}
