package com.ligen.framework.loader;

import com.ligen.framework.annotation.RequestMapping;
import com.ligen.framework.bean.Handler;
import com.ligen.framework.bean.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ligen on 2017/5/29.
 */
public class ControllerLoader {

    private static Logger log = LoggerFactory.getLogger(ControllerLoader.class);
    private static Map<Request, Handler> REQUEST_MAP = new HashMap<>();

    static {
        log.info("ControllerLoader inited");
        Set<Class<?>> controllerClassSet = ClassLoader.getControllerClassSet();
        for (Class<?> cls : controllerClassSet) {
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String mapValue = requestMapping.value().toLowerCase();
                    //TODO 首字母斜杠都移除，暂不考虑多级requestmapping问题
                    if (mapValue.startsWith("/")) {
                        mapValue = mapValue.substring(1);
                    }
                    String mapMethod = requestMapping.method().toLowerCase();
                    if (mapMethod == null) {
                        mapMethod = "get";
                    }
                    Request request = new Request(mapMethod, mapValue);
                    Handler handler = new Handler(cls, method);
                    REQUEST_MAP.put(request, handler);
                }
            }

        }

    }

    public static void init() {}

    public static Handler getHandler(String method, String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Request request = new Request(method, path);
        return REQUEST_MAP.get(request);
    }

}
