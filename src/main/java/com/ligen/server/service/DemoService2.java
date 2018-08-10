package com.ligen.server.service;

import com.ligen.framework.annotation.Autowired;
import com.ligen.framework.annotation.stereotype.Service;

/**
 * Created by ligen on 2018/8/10.
 */
@Service
public class DemoService2 {

    @Autowired
    DemoService1 demoService1;

    public String hello(String name) {
        return "Hello, " + name;
    }

    public String helloFromOther(String name) {
        return demoService1.hello(name);
    }

}
