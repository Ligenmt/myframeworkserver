package com.ligen.framework;


import com.alibaba.fastjson.JSON;
import com.ligen.framework.bean.Data;
import com.ligen.framework.bean.Handler;
import com.ligen.framework.bean.Params;
import com.ligen.framework.helper.BeanHelper;
import com.ligen.framework.helper.ControllerHelper;
import com.ligen.framework.util.*;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by com.com.com.ligen on 2017/5/29.
 */
public class DispatchServlet extends HttpServlet {

    public DispatchServlet() {
        System.out.println("DispatchServlet");
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        HelperLoader.init();
//        String appBasePackage = ConfigHelper.getAppBasePackage();
//        System.out.println(appBasePackage);
        ServletContext servletContext = servletConfig.getServletContext();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String method = req.getMethod().toLowerCase();
        String path = req.getRequestURI();

        //根据路由找到相应的Controller
        Handler handler = ControllerHelper.getHandler(method, path);
        Class<?> controllerClass = handler.getControllerClass();
        Object controllerBean = BeanHelper.getBean(controllerClass);
        Map<String, String> requestParams = new HashMap<>();
        //处理请求的携带参数
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = req.getParameter(name);
            requestParams.put(name, value);
        }
        //处理请求体
        String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
        if(!StringUtils.isEmpty(body)) {
            if(req.getHeader("Content-Type").equals("application/x-www-form-urlencoded")) {
                String[] postparams = body.split("&");
                if(postparams.length > 0) {
                    for (String param : postparams) {
                        String key = param.split("=")[0];
                        String value = param.split("=")[1];
                        requestParams.put(key, value);
                    }
                }
            } else if(req.getHeader("Content-Type").equals("application/json")) {
                //TODO json格式请求体
            } else {
                //其它
            }
        }
        Method mappingMethod = handler.getMappingMethod();
        //注入路由方法参数
        Class<?>[] parameterTypes = mappingMethod.getParameterTypes();
        Object args[] = null;
        if (parameterTypes.length > 0) {
            args = new Object[parameterTypes.length];
            for (int i=0; i<parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                String canonicalName = parameterType.getCanonicalName();
                if (canonicalName.equals("javax.servlet.http.HttpServletRequest")) {
                    args[i] = req;
                } else if (canonicalName.equals("javax.servlet.http.HttpServletResponse")) {
                    args[i] = resp;
                }


            }
        }
        Params p = new Params(requestParams);

        Object o = ReflectionUtil.invokeMethod(controllerBean, mappingMethod, args);
        if(o instanceof Data) {
            Data data = (Data) o;
            Object model = data.getModel();
            if(model != null) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("utf-8");
                PrintWriter writer = resp.getWriter();
                String jsonStr = JSON.toJSONString(model);
                writer.write(jsonStr);
                writer.flush();
                writer.close();
            }
        } else if(o instanceof String) {
            resp.setCharacterEncoding("utf-8");
            PrintWriter writer = resp.getWriter();
            writer.write((String) o);
            writer.flush();
            writer.close();
        }

    }
}
