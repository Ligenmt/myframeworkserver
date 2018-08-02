package com.ligen.framework.helper;

import com.ligen.framework.annotation.Autowired;
import com.ligen.framework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by com.com.com.ligen on 2017/5/29.
 */
public class IocHelper {

    static Logger log = LoggerFactory.getLogger(IocHelper.class);
    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
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
        log.info("IocHelper inited");
    }
    public static void init(){}
}
