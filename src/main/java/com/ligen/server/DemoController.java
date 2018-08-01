package com.ligen.server;

import com.alibaba.fastjson.JSONObject;
import com.ligen.framework.annotation.Controller;
import com.ligen.framework.annotation.RequestMapping;
import com.ligen.framework.annotation.RequestParam;
import com.ligen.framework.bean.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ligen on 2017/12/25.
 */
@Controller
public class DemoController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/a", method = "get")
    public Data a() {
        Data data = new Data();
        return data;
    }

    @RequestMapping(value = "/", method = "get")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        logger.info("index name:{}", name);
        return "hello, " + name;
    }

    @RequestMapping(value = "/index", method = "POST")
    public String indexPost(@RequestParam(value = "name") String name) {
        return "hello, " + name;
    }
}