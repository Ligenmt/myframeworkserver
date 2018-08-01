package com.ligen.framework.util;

import com.ligen.framework.helper.BeanHelper;
import com.ligen.framework.helper.ClassHelper;
import com.ligen.framework.helper.ControllerHelper;
import com.ligen.framework.helper.IocHelper;

/**
 * Created by com.com.com.ligen on 2017/5/29.
 */
public class HelperLoader {


    static {
        System.out.println("HelperLoader");
    }

    public static void init() {
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
    }
}
