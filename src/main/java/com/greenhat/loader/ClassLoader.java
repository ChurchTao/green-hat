package com.greenhat.loader;



import com.greenhat.annotation.Bean;
import com.greenhat.annotation.Controller;
import com.greenhat.annotation.Service;
import com.greenhat.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public final class ClassLoader {

    private static final List<Class<?>> CLASS_LIST;

    static {
        String basePackage= ConfigLoader.getAppBasePackage();
        CLASS_LIST = ClassUtil.getClassList(basePackage);
    }

    public static List<Class<?>> getClassList() {
        return CLASS_LIST;
    }

    public static List<Class<?>>  getServiceClasses(){
        List<Class<?>> classes=new ArrayList<Class<?>>();
        for (Class<?> cls:CLASS_LIST){
            if (cls.isAnnotationPresent(Service.class)){
                classes.add(cls);
            }
        }
        return classes;
    }

    public static List<Class<?>>  getControllerClasses(){
        List<Class<?>> classes=new ArrayList<Class<?>>();
        for (Class<?> cls:CLASS_LIST){
            if (cls.isAnnotationPresent(Controller.class)){
                classes.add(cls);
            }
        }
        return classes;
    }

    public static List<Class<?>>  getBeanClasses(){
        List<Class<?>> classes=new ArrayList<Class<?>>();
        for (Class<?> cls:CLASS_LIST){
            if (cls.isAnnotationPresent(Bean.class)){
                classes.add(cls);
            }
        }
        return classes;
    }

    /**
     * 获取包名下某  父类【或接口】  的所有  子类【或实现类】
     * @param superClass
     * @return
     */
    public static List<Class<?>>  getClassListBySuper(Class<?> superClass){
        List<Class<?>> classes=new ArrayList<Class<?>>();
        for (Class<?> cls:CLASS_LIST){
            if (superClass.isAssignableFrom(cls)&&!superClass.equals(cls)){
                classes.add(cls);
            }
        }
        return classes;
    }

    /**
     * 获取应用包名下带有【某注解】的所有类
     * @param annotationClass
     * @return
     */
    public static List<Class<?>>  getClassListByAnnotation(Class<? extends Annotation> annotationClass){
        List<Class<?>> classes=new ArrayList<Class<?>>();
        for (Class<?> cls:CLASS_LIST){
            if (cls.isAnnotationPresent(annotationClass)){
                classes.add(cls);
            }
        }
        return classes;
    }

}
