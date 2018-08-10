package com.ligen.framework.loader;

import com.ligen.framework.annotation.Autowired;
import com.ligen.framework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
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
            Field[] fields = beanClass.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    if(field.isAnnotationPresent(Autowired.class)) {
                        Class<?> fieldType = field.getType();
                        Object fieldInstance = beanMap.get(fieldType);
                        if (fieldInstance != null) {
                            ReflectionUtil.setField(beanInstance, field, fieldInstance);
                        }
                    }
                }
            }
        }
        log.info("IocLoader初始化完成");
    }
    public static void init(){}
}
