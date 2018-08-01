package com.ligen.framework.bean;

import java.lang.reflect.Method;

/**
 * Created by com.com.com.ligen on 2017/5/29.
 */
public class Handler {

    private Class<?> controllerClass;

    private Method mappingMethod;

    public Handler(Class<?> controllerClass, Method mappingMethod) {
        this.controllerClass = controllerClass;
        this.mappingMethod = mappingMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getMappingMethod() {
        return mappingMethod;
    }


}
