package com.greenhat.mvc;

import com.greenhat.Config;
import com.greenhat.GreenHatLoader;
import com.greenhat.loader.ConfigLoader;
import com.greenhat.util.ClassUtil;
import com.greenhat.util.StringUtil;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import javassist.ClassPool;
import javassist.LoaderClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created by jiacheng on 2017/7/25.
 */
@WebListener
public class ServletInitListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ServletInitListener.class);


    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // 获取 ServletContext
        ServletContext servletContext = servletContextEvent.getServletContext();
        // 初始化相关 Helper 类
        GreenHatLoader.init();
        addServletMapping(servletContext);
        UploadHelper.init(servletContext);
    }

//    private void addFilter(ServletContext servletContext) {
//        servletContext.addFilter("Cross",CorsFilter.class);
//        logger.info("Cross init OK !");
//    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //手动取消注册的 数据库驱动
        avoidGarbageCollectionWarning();
    }

    private void addServletMapping(ServletContext context) {
        // 用 DefaultServlet 映射所有静态资源
        registerDefaultServlet(context);
        // 用 JspServlet 映射所有 JSP 请求
        registerJspServlet(context);
        logger.info("All static pages and resources init OK!");
    }

    private void registerDefaultServlet(ServletContext context) {
        ServletRegistration defaultServlet = context.getServletRegistration("default");
        defaultServlet.addMapping("/index.html");
        defaultServlet.addMapping(Config.APP_ASSET_PATH + "*");
        defaultServlet.addMapping("/favicon.ico");
        String wwwPath = Config.APP_WWW_PATH;
        if (StringUtil.isNotEmpty(wwwPath)) {
            defaultServlet.addMapping(wwwPath + "*");
        }
    }

    private void registerJspServlet(ServletContext context) {
        ServletRegistration jspServlet = context.getServletRegistration("jsp");
        jspServlet.addMapping("/index.jsp");
        String jspPath = Config.APP_JSP_PATH;
        if (StringUtil.isNotEmpty(jspPath)) {
            jspServlet.addMapping(jspPath + "*");
        }
    }
    private void avoidGarbageCollectionWarning()
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        while (drivers.hasMoreElements()) {
            try {
                d = drivers.nextElement();
                if(d.getClass().getClassLoader() == cl) {
                    DriverManager.deregisterDriver(d);
                    logger.info("Driver {} unregistered", d);
                }
                else {
                    logger.info("Driver {} not unregistered because it might be in use else where", d.toString());
                }
            }
            catch (SQLException ex) {
                logger.warn(String.format("Error unregistering driver %s, exception: %s", d.toString(), ex.toString()));
            }
        }
        try {
            AbandonedConnectionCleanupThread.shutdown();
        }
        catch (InterruptedException e) {
            logger.warn("SEVERE problem cleaning up: " + e.getMessage());
        }
    }
}
