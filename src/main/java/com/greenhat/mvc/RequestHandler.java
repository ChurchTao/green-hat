package com.greenhat.mvc;


import com.greenhat.ConfigNames;
import com.greenhat.loader.BeanLoader;
import com.greenhat.loader.ConfigLoader;
import com.greenhat.mvc.bean.Handler;
import com.greenhat.mvc.bean.Param;
import com.greenhat.mvc.bean.View;
import com.greenhat.mvc.request.ContentType;
import com.greenhat.mvc.request.JsonReader;
import com.greenhat.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by jiacheng on 2017/7/25.
 */
public class RequestHandler {

    public void doHandel(HttpServletRequest req, HttpServletResponse res, Handler handler) throws Exception {
        Class<?> controllerClass = handler.getControllerClass();
        Object controllerBean = BeanLoader.getBean(controllerClass);

        Param param;
        if (UploadHelper.isMultipart(req)) {
            param = UploadHelper.createParam(req);
        } else {
            if (req.getContentType()==null){
                Map<String, Object> paramMap = WebUtil.getRequestParamMap(req);
                param = new Param(paramMap);
            }else {
                if (req.getContentType().contains(ContentType.JSON.toString())){
                    Map map = JsonReader.receiveJson(req);
                    param = new Param(map);
                }else if (req.getContentType().contains(ContentType.FORM.toString())){
                    Map<String, Object> paramMap = WebUtil.getRequestParamMap(req);
                    param = new Param(paramMap);
                }else {
                    throw new RuntimeException("请求未知，已阻拦");
                }
            }
        }

        Method actionMethod = handler.getActionMethod();
        actionMethod.setAccessible(true); // 取消类型安全检测（可提高反射性能）
        Object result = executeMethod(controllerBean, actionMethod, req, res, param);
        res.setHeader("X-Powered-By", "GreenHat(" + ConfigNames.VERSION + ")");

        if (result instanceof View) {
            View view = (View) result;
            if (view.isRedirect()) {
                String path = view.getPath();
                WebUtil.redirectRequest(path, req, res);
            } else {
                String path = ConfigLoader.getAppJspPath() + view.getPath();
                Map<String, Object> data = view.getModel();
                if (MapUtil.isNotEmpty(data)) {
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                }
                WebUtil.forwardRequest(path, req, res);
            }
        } else if (result instanceof String) {
            String path = (String) result;
            if (!path.equals("")) {
                WebUtil.forwardRequest(ConfigLoader.getAppWwwPath()+path, req, res);
            }
        } else {
            Class<?> resultClass = actionMethod.getReturnType();
            if (UploadHelper.isMultipart(req)) {
                // 对于 multipart 类型，说明是文件上传，需要转换为 HTML 格式并写入响应中
                WebUtil.writeHTML(res, result);
            } else {
                // 对于其它类型，统一转换为 JSON 格式并写入响应中
                WebUtil.writeJSON(res, result,resultClass);
            }
        }
    }


    /**
     * 获取方法内的参数
     */
    private Object[] getArgs(HttpServletRequest request, HttpServletResponse response, Param param, Class<?>[] params) {

        int len = params.length;
        Object[] args = new Object[len];

        for (int i = 0; i < len; i++) {
            Class<?> paramTypeClazz = params[i];
            if (paramTypeClazz.getName().equals(HttpServletRequest.class.getName())) {
                args[i] = request;
            }
            if (paramTypeClazz.getName().equals(HttpServletResponse.class.getName())) {
                args[i] = response;
            }
            if (paramTypeClazz.getName().equals(Param.class.getName())) {
                args[i] = param;
            }
        }
        return args;
    }

    /**
     * 执行路由方法
     */
    private Object executeMethod(Object object, Method method, HttpServletRequest request, HttpServletResponse response, Param param) {
        int len = method.getParameterTypes().length;
        method.setAccessible(true);
        if (len > 0) {
            Object[] args = getArgs(request, response, param, method.getParameterTypes());
            return ReflectUtil.invokeMehod(object, method, args);
        } else {
            return ReflectUtil.invokeMehod(object, method);
        }
    }
}
