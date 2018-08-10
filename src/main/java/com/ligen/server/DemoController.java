package com.ligen.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ligen.framework.annotation.Controller;
import com.ligen.framework.annotation.PostConstruct;
import com.ligen.framework.annotation.RequestMapping;
import com.ligen.framework.annotation.RequestParam;
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

    @PostConstruct
    public void init() {
        System.out.println("DemoController inited");
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

    @RequestMapping(value = "/json", method = "POST")
    public String json(JSONObject json) {
        return json.toJSONString();
    }

    @RequestMapping(value = "/jsonarray", method = "POST")
    public String jsonArray(JSONArray json) {
        return json.toJSONString();
    }
}
