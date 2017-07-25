package com.greenhat.mvc.bean;

import java.lang.reflect.Method;

/**
 * Created by jiacheng on 2017/7/19.
 */
public class Handler {
    private Class<?> controllerClass;
    private Method actionMethod;
   // private Matcher requestPathMatcher;



    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod=actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

//    public void setControllerClass(Class<?> controllerClass) {
//        this.controllerClass = controllerClass;
//    }
//
//    public Matcher getRequestPathMatcher() {
//        return requestPathMatcher;
//    }
//
//    public void setActionMethod(Method actionMethod) {
//        this.actionMethod = actionMethod;
//    }
//
//    public void setRequestPathMatcher(Matcher requestPathMatcher) {
//        this.requestPathMatcher = requestPathMatcher;
//    }
}
