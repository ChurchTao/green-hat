package com.greenhat.annotation;

import com.greenhat.mvc.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jiacheng on 2017/7/19.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {
    String value() default "/";
    RequestMethod method() default RequestMethod.get;
}
