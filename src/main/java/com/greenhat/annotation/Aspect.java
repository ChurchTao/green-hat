package com.greenhat.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 包名
     */
    String pkg() default "";

    /**
     * 类名
     */
    String cls() default "";

    /**
     * 注解
     *
     * @since 2.2
     */
    Class<? extends Annotation> value() default Aspect.class;
}
