package com.ligen.framework.helper;

import com.ligen.framework.annotation.Controller;
import com.ligen.framework.annotation.Service;
import com.ligen.framework.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ligen on 2017/5/28.
 */
public final class ClassHelper {

    static Logger log = LoggerFactory.getLogger(ClassHelper.class);

    private static Set<Class<?>> CLASS_SET;
    //TODO 从这里开始
    static {
        log.info("ClassHelper初始化开始");
        String basePackage = ConfigHelper.getAppBasePackage();
        log.info("扫描包路径:" + basePackage);
        CLASS_SET = ClassUtil.getClassSet(basePackage);
        log.info("ClassHelper初始化完成");
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Service.class)) {
                set.add(cls);
            }
        }
        return set;
    }

    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Controller.class)) {
                set.add(cls);
            }
        }
        return set;
    }
    //注入Controller和Service
    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.addAll(getServiceClassSet());
        set.addAll(getControllerClassSet());
        return set;
    }



}
