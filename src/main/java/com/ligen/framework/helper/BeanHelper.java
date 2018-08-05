package com.ligen.framework.helper;

import com.ligen.framework.annotation.PostConstruct;
import com.ligen.framework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * bean助手类
 * Created by ligen on 2017/5/29.
 */
public class BeanHelper {

    private static final Logger log = LoggerFactory.getLogger(BeanHelper.class);
    private static Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    static {
        log.info("BeanHelper初始化开始");
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> beanCls : beanClassSet) {
            Object o = ReflectionUtil.newInstance(beanCls);
            try {
                Method[] methods = beanCls.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(PostConstruct.class)) {
                        method.invoke(o, null);
                        break;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("执行初始化方法失败", e);
            }
            BEAN_MAP.put(beanCls, o);
        }
        log.info("BeanHelper初始化完成");
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    public static <T> T getBean(Class<T> cls) {
        return (T) BEAN_MAP.get(cls);
    }
}
