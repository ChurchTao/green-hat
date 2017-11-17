package com.greenhat.mvc.bean;

import java.lang.reflect.Method;

/**
 * Created by jiacheng on 2017/7/19.
 */
public class Handler {
    private boolean isJsonRequest;
    private Class<?> controllerClass;
    private Method actionMethod;

    public Handler(boolean isJsonRequest,Class<?> controllerClass, Method actionMethod) {
        this.isJsonRequest=isJsonRequest;
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public boolean isJsonRequest() {
        return isJsonRequest;
    }
}
