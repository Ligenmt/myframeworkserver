package com.ligen.framework.loader;

import com.ligen.framework.annotation.Autowired;
import com.ligen.framework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by ligen on 2017/5/29.
 */
public class IocLoader {

    static Logger log = LoggerFactory.getLogger(IocLoader.class);
    static {
        log.info("IocLoader初始化开始");
        Map<Class<?>, Object> beanMap = BeanLoader.getBeanMap();
        for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
            Class<?> beanClass = entry.getKey();
            Object beanInstance = entry.getValue();
            try {
                //实现@Autowired注入Field，处理循环引用
                Field[] declaredFields = beanClass.getDeclaredFields();
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Class<?> injectingClass = field.getType();
                        Object injectingObject = beanMap.get(injectingClass);
                        if (injectingObject != null) {
                            field.setAccessible(true);
                            field.set(beanInstance, injectingObject);
                        } else {
                            throw new RuntimeException("自动注入实例失败:" + injectingClass.getName());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("自动注入实例失败:" + beanClass.getName(), e);
            }
            //构造完成调用方法
            try {
                Method[] methods = beanClass.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(PostConstruct.class)) {
                        method.invoke(beanInstance, null);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("执行初始化方法失败:" + beanClass.getName(), e);
            }
        }
        log.info("IocLoader初始化完成");
    }
    public static void init(){}
}
