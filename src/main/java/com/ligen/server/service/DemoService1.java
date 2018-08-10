package com.ligen.server.service;

import com.ligen.framework.annotation.Autowired;
import com.ligen.framework.annotation.stereotype.Service;

/**
 * Created by ligen on 2018/8/10.
 */
@Service
public class DemoService1 {

    @Autowired
    DemoService2 demoService2;

    public String hello(String name) {
        return "Hello, " + name;
    }

    public String helloFromOther(String name) {
        return demoService2.hello(name);
    }

}
