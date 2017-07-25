package com.greenhat.mvc;


import com.greenhat.ConfigNames;
import com.greenhat.helper.ControllerHelper;
import com.greenhat.mvc.bean.Handler;
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
    public void init() throws ServletException {

    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // 设置请求编码方式
        req.setCharacterEncoding(ConfigNames.UTF_8);
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();

        logger.debug("[Green Hat] {}:{}", requestMethod, requestPath);
        // 将“/”请求重定向到首页
        if (requestMethod.equals("/")) {
            WebUtil.redirectRequest(ConfigNames.HOME_PAGE, req, res);
            return;
        }
        // 去掉当前请求路径末尾的“/”
        if (requestMethod.endsWith("/")) {
            requestMethod = requestPath.substring(0, requestPath.length() - 1);
        }

        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);

        if (handler == null) {
            WebUtil.sendError(HttpServletResponse.SC_NOT_FOUND, "", res);
            return;
        }
        // 初始化 DataContext
        DataContext.init(req, res);
        try {
            // 调用 Handler
            requestHandler.doHandel(req, res, handler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 销毁 DataContext
            DataContext.destroy();
        }
    }

}
