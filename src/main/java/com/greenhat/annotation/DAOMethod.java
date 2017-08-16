package com.greenhat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jiacheng on 2017/8/8.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DAOMethod {
    String sql() default "";
    int start() default 0;
    int limit() default 100;
    String orderBy() default "";
}
