package com.ligen.framework.util;

import com.ligen.framework.loader.BeanLoader;
import com.ligen.framework.loader.ClassLoader;
import com.ligen.framework.loader.ControllerLoader;
import com.ligen.framework.loader.IocLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Created by ligen on 2017/5/29.
 */
public class Bootstrap {

    static Logger log = LoggerFactory.getLogger(Bootstrap.class);
    static {
        log.info("Bootstrap初始化");
    }
    //从这里开始初始化
    public static void init() {
        log.info("Bootstrap初始化开始");
        Class<?>[] classList = {
            ClassLoader.class,
            BeanLoader.class,
            IocLoader.class,
            ControllerLoader.class
        };

        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(), true);
        }
//        IocLoader.init();
//        ControllerLoader.init();
        log.info("Bootstrap初始化结束");
    }
}
