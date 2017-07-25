package com.greenhat.mvc;



import com.greenhat.GreenHatLoader;
import com.greenhat.loader.ConfigLoader;
import com.greenhat.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

/**
 * Created by jiacheng on 2017/7/25.
 */
@WebListener
public class ServletInitListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // 获取 ServletContext
        ServletContext servletContext = servletContextEvent.getServletContext();
        // 初始化相关 Helper 类
        GreenHatLoader.init();
        addServletMapping(servletContext);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private void addServletMapping(ServletContext context) {
        // 用 DefaultServlet 映射所有静态资源
        registerDefaultServlet(context);
        // 用 JspServlet 映射所有 JSP 请求
        registerJspServlet(context);
    }

    private void registerDefaultServlet(ServletContext context) {
        ServletRegistration defaultServlet = context.getServletRegistration("default");
        defaultServlet.addMapping("/index.html");
        defaultServlet.addMapping(ConfigLoader.getAppAssetPath() + "*");
        defaultServlet.addMapping("/favicon.ico");
        String wwwPath = ConfigLoader.getAppWwwPath();
        if (StringUtil.isNotEmpty(wwwPath)) {
            defaultServlet.addMapping(wwwPath + "*");
        }
    }

    private void registerJspServlet(ServletContext context) {
        ServletRegistration jspServlet = context.getServletRegistration("jsp");
        jspServlet.addMapping("/index.jsp");
        String jspPath = ConfigLoader.getAppJspPath();
        if (StringUtil.isNotEmpty(jspPath)) {
            jspServlet.addMapping(jspPath + "*");
        }
    }
}
