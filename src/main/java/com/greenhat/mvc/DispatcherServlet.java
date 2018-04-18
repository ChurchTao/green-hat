package com.greenhat.mvc;


import com.greenhat.Config;
import com.greenhat.loader.ConfigLoader;
import com.greenhat.loader.ControllerLoader;
import com.greenhat.mvc.bean.Handler;
import com.greenhat.mvc.fault.ServerException;
import com.greenhat.mvc.request.RestResponse;
import com.greenhat.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jiacheng on 2017/7/20.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private RequestHandler requestHandler = new RequestHandler();

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // 设置请求编码方式
        req.setCharacterEncoding(Config.UTF_8);
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();

        if ("true".equals(Config.ALLOW_CORS)&&"options".equalsIgnoreCase(requestMethod)){
            return;
        }
        logger.debug("[Green Hat] {}:{}", requestMethod, requestPath);
        // 将“/”请求重定向到首页
        if (requestPath.equals("/")||requestPath.equals("")) {
            WebUtil.redirectRequest(Config.HOME_PAGE, req, res);
            return;
        }
        if (!requestPath.startsWith("/")) {
            requestPath = "/"+requestPath;
        }
        // 去掉当前请求路径末尾的“/”
        if (requestPath.endsWith("/")) {
            requestPath = requestPath.substring(0, requestPath.length() - 1);
        }

        Handler handler = null;
        try {
            handler = ControllerLoader.getHandler(requestMethod, requestPath,req);
        } catch (Exception e) {
            if (e instanceof ServerException){
                WebUtil.writeJSON(res, RestResponse.fail(((ServerException) e).getCode(),e.getMessage()),RestResponse.class);
                return;
            }
            logger.error("请检查 {}",e);
            return;
        }

        if (handler == null) {
            String path_404 = ConfigLoader.getString(Config.APP_PATH_404);
            if (path_404!=null&&!path_404.equals("")){
                WebUtil.forwardRequest(Config.APP_WWW_PATH+path_404, req, res);
                return;
            }else {
                WebUtil.sendError(HttpServletResponse.SC_NOT_FOUND, "", res);
                return;
            }
        }
        // 初始化 DataContext
        DataContext.init(req, res);
        try {
            // 调用 Handler
            logger.info("Handled {}",requestMethod+":/"+requestPath);
            requestHandler.doHandel(req, res, handler);
        } catch (Exception e) {
            if (e instanceof ServerException){
                WebUtil.writeJSON(res, RestResponse.fail(((ServerException) e).getCode(),e.getMessage().substring(39)),RestResponse.class);
                return;
            }
            logger.error("请检查config.properties 是否设置正确 {}",e);
        } finally {
            // 销毁 DataContext
            DataContext.destroy();
        }
    }

}
