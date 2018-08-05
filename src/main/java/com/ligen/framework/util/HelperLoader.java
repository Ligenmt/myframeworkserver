package com.ligen.framework.util;

import com.ligen.framework.helper.BeanHelper;
import com.ligen.framework.helper.ClassHelper;
import com.ligen.framework.helper.ControllerHelper;
import com.ligen.framework.helper.IocHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by com.com.com.ligen on 2017/5/29.
 */
public class HelperLoader {

    static Logger log = LoggerFactory.getLogger(HelperLoader.class);
    static {
        log.info("HelperLoader初始化");
    }

    public static void init() {
        log.info("HelperLoader初始化开始");
        Class<?>[] classList = {
            ClassHelper.class,
            BeanHelper.class,
            IocHelper.class,
            ControllerHelper.class
        };

        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(), false);
        }
        IocHelper.init();
        ControllerHelper.init();
        log.info("HelperLoader初始化结束");
    }
}
