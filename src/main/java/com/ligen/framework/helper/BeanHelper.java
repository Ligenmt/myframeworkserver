package com.ligen.framework.helper;

import com.ligen.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * bean助手类
 * Created by com.com.com.ligen on 2017/5/29.
 */
public class BeanHelper {

    private static Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> beanCls : beanClassSet) {
            Object o = ReflectionUtil.newInstance(beanCls);
            BEAN_MAP.put(beanCls, o);
        }
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    public static <T> T getBean(Class<T> cls) {
        return (T) BEAN_MAP.get(cls);
    }


}
