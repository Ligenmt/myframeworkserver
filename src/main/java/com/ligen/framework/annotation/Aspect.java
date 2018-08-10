package com.ligen.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by ligen on 2018/8/2.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    Class<? extends Annotation> value();

}
