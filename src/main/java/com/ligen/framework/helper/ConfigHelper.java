package com.ligen.framework.helper;

import com.ligen.framework.util.ConfigConstant;
import com.ligen.framework.util.PropsUtil;

import java.util.Properties;

/**
 * Created by ligen on 2017/5/29.
 */
public final class ConfigHelper {


    private static Properties CONFIG_PROPS;

    static {
        CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);
    }

    //获取自动注入包扫描根路径
    public static String getAppBasePackage() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.BASE_PACKAGE);
    }
}
