package com.ligen.framework.util;

import java.io.InputStream;
import java.util.Properties;


/**
 * Created by ligen on 2017/5/29.
 */
public class PropsUtil {

    public static Properties loadProps(String propertyName) {
        Properties properties = new Properties();

        InputStream in = null;
        try {
//            in = new FileInputStream(propertyName);
            in = PropsUtil.class.getResourceAsStream(propertyName);
            if (in == null) {
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyName);
            }
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;

    }

    public static String getString(Properties properties, String key) {
        return properties.getProperty(key);
    }
}
