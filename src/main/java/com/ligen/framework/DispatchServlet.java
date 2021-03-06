package com.ligen.framework;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ligen.framework.annotation.RequestParam;
import com.ligen.framework.bean.Handler;
import com.ligen.framework.loader.BeanLoader;
import com.ligen.framework.loader.ControllerLoader;
import com.ligen.framework.util.CodecUtil;
import com.ligen.framework.util.Bootstrap;
import com.ligen.framework.util.ReflectionUtil;
import com.ligen.framework.util.StreamUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ligen on 2017/5/29.
 */
public class DispatchServlet extends HttpServlet {

    Logger log = LoggerFactory.getLogger(DispatchServlet.class);
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        log.info("DispatchServlet init start");
        Bootstrap.init();
        ServletContext servletContext = servletConfig.getServletContext();
        log.info("DispatchServlet inited");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String method = req.getMethod().toLowerCase();
        String path = req.getRequestURI();

        //根据路由找到相应的Controller
        Handler handler = ControllerLoader.getHandler(method, path);
        Class<?> controllerClass = handler.getControllerClass();
        Object controllerBean = BeanLoader.getBean(controllerClass);
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
        JSONObject jsonInput = null;
        JSONArray jsonArrayInput = null;
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
            } else if(req.getHeader("Content-Type").startsWith("application/json")) {
                try {
                    jsonInput = JSON.parseObject(body);
                } catch (Exception e) {
                    try {
                        jsonArrayInput = JSON.parseArray(body);
                    } catch (Exception e1) {
                        throw new RuntimeException("请求体JSON格式非法:" + body, e);
                    }
                }
            } else {
                //其它
            }
        }
        Method mappingMethod = handler.getMappingMethod();
        //注入路由方法参数
        Class<?>[] parameterTypes = mappingMethod.getParameterTypes();
        Parameter[] parameters = mappingMethod.getParameters();
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
                } else if (canonicalName.equals("com.alibaba.fastjson.JSONObject")) {
                    args[i] = jsonInput;
                } else if (canonicalName.equals("com.alibaba.fastjson.JSONArray")) {
                    args[i] = jsonArrayInput;
                }
                if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                    RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
                    String value = requestParam.value();
                    String v = requestParams.get(value);
                    if (v != null) {
                        args[i] = v;
                    }
                }
            }
        }
//        Params p = new Params(requestParams);
        Object o = ReflectionUtil.invokeMethod(controllerBean, mappingMethod, args);
        if(o instanceof JSONObject) {
            JSONObject data = (JSONObject) o;
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            PrintWriter writer = resp.getWriter();
            String jsonStr = data.toJSONString();
            writer.write(jsonStr);
            writer.flush();
            writer.close();
        } else if (o instanceof String) {
            resp.setCharacterEncoding("utf-8");
            PrintWriter writer = resp.getWriter();
            writer.write((String) o);
            writer.flush();
            writer.close();
        }

    }
}
