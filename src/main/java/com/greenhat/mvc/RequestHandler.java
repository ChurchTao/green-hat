package com.greenhat.mvc;



import com.greenhat.loader.BeanLoader;
import com.greenhat.loader.ConfigLoader;
import com.greenhat.mvc.bean.Data;
import com.greenhat.mvc.bean.Handler;
import com.greenhat.mvc.bean.Param;
import com.greenhat.mvc.bean.View;
import com.greenhat.util.JsonUtil;
import com.greenhat.util.ReflectUtil;
import com.greenhat.util.StringUtil;
import com.greenhat.util.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by jiacheng on 2017/7/25.
 */
public class RequestHandler {

    public void doHandel(HttpServletRequest req, HttpServletResponse res, Handler handler) throws Exception{
        Class<?> controllerClass = handler.getControllerClass();
        Object controllerBean = BeanLoader.getBean(controllerClass);

        Map<String, Object> paramMap = WebUtil.getRequestParamMap(req);

        Param param = new Param(paramMap);
        Method actionMethod = handler.getActionMethod();
        actionMethod.setAccessible(true); // 取消类型安全检测（可提高反射性能）
        Object result = executeMethod(controllerBean,actionMethod,req,res,param);


        if (result instanceof View) {
            View view = (View) result;
            String path = view.getPath();
            if (StringUtil.isNotEmpty(path)){
                Map<String,Object> model = view.getModel();
                for (Map.Entry<String,Object>entry:model.entrySet()){
                    req.setAttribute(entry.getKey(),entry.getValue());
                }
                req.getRequestDispatcher(ConfigLoader.getAppJspPath()+path).forward(req,res);

            }
        } else if (result instanceof Data) {
            Data data = (Data) result;
            Object model = data.getModel();
            if (model!=null){
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                PrintWriter writer = res.getWriter();
                String json = JsonUtil.toJSON(model);
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }else if (result instanceof String){
            String path = (String)result;
            if (!path.equals("")){
                req.getRequestDispatcher(ConfigLoader.getAppWwwPath()+path).forward(req,res);
            }
        }
    }



    /**
     * 获取方法内的参数
     */
    private Object[] getArgs(HttpServletRequest request, HttpServletResponse response, Param param, Class<?>[] params){

        int len = params.length;
        Object[] args = new Object[len];

        for(int i=0; i<len; i++){
            Class<?> paramTypeClazz = params[i];
            if(paramTypeClazz.getName().equals(HttpServletRequest.class.getName())){
                args[i] = request;
            }
            if(paramTypeClazz.getName().equals(HttpServletResponse.class.getName())){
                args[i] = response;
            }
            if(paramTypeClazz.getName().equals(Param.class.getName())){
                args[i] = param;
            }
        }
        return args;
    }

    /**
     * 执行路由方法
     */
    private Object executeMethod(Object object, Method method, HttpServletRequest request, HttpServletResponse response, Param param){
        int len = method.getParameterTypes().length;
        method.setAccessible(true);
        if(len > 0){
            Object[] args = getArgs(request, response,param, method.getParameterTypes());
            return ReflectUtil.invokeMehod(object, method, args);
        } else {
            return ReflectUtil.invokeMehod(object, method);
        }
    }
}
