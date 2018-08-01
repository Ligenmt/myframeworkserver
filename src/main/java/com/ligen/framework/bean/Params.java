package com.ligen.framework.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by com.com.com.ligen on 2017/5/31.
 */
public class Params {

    private Map<String, String> paramMap = new HashMap<>();

    public Params(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Object getValue(String key) {
        return paramMap.get(key);
    }

    public Map<String, String> getMap() {
        return paramMap;
    }


}
