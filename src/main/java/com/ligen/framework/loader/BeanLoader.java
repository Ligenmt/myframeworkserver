package com.ligen.framework.loader;

import com.ligen.framework.annotation.Autowired;
import com.ligen.framework.annotation.PostConstruct;
import com.ligen.framework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 实例bean加载类
 * Created by ligen on 2017/5/29.
 */
public class BeanLoader {

    private static final Logger log = LoggerFactory.getLogger(BeanLoader.class);
    private static Map<Class<?>, Object> BEAN_MAP = new HashMap<>(16);
    private static Map<String, Object> EARLY_SINGLETON_OBJECTS = new HashMap<String, Object>(16);

    static {
        log.info("BeanLoader初始化开始");
        //获取所有需要实例化的类，目前包括: @Controller,@Service注解的类
        Set<Class<?>> beanClassSet = ClassLoader.getBeanClassSet();
        for (Class<?> beanCls : beanClassSet) {
            Object o = ReflectionUtil.newInstance(beanCls);
            EARLY_SINGLETON_OBJECTS.put(beanCls.getName(), o);
        }
        for (Class<?> beanCls : beanClassSet) {
            Object o = EARLY_SINGLETON_OBJECTS.get(beanCls.getName());
            //实现@Autowired注入Field，EARLY_SINGLETON_OBJECTS处理循环引用
            try {
                Field[] declaredFields = beanCls.getDeclaredFields();
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Class<?> injectingClass = field.getType();
                        Object injectingObject = EARLY_SINGLETON_OBJECTS.get(injectingClass.getName());
                        if (injectingObject != null) {
                            field.setAccessible(true);
                            field.set(o, injectingObject);
                        } else {
                            throw new RuntimeException("自动注入实例失败:" + injectingClass.getName());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("自动注入实例失败:" + beanCls.getName(), e);
            }
            try {
                Method[] methods = beanCls.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(PostConstruct.class)) {
                        method.invoke(o, null);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("执行初始化方法失败:" + beanCls.getName(), e);
            }
            BEAN_MAP.put(beanCls, o);
        }
        EARLY_SINGLETON_OBJECTS.clear();
        log.info("BeanLoader初始化完成");
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    public static <T> T getBean(Class<T> cls) {
        return (T) BEAN_MAP.get(cls);
    }
}
