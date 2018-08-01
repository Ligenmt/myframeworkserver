package com.ligen.framework.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回视图对象
 * Created by com.com.com.ligen on 2017/5/31.
 */
public class View {

    private String path;
    private Map<String, Object> model;

    public View(String path) {
        this.path = path;
        this.model = new HashMap<>();
    }

    public View addModel(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
