package com.ligen.framework.util;

import com.ligen.framework.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ligen on 2017/5/28.
 */
public class ClassUtil {

//    private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类,初始化是指是否执行类的静态代码块
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            //isInitialized为true则执行初始化，执行static静态方法，false则不执行
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cls;

    }

    /**
     * 获取指定包名下所有类
     */
    public static Set<Class<?>> getClassSet(String packageName) {

        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> resources = getClassLoader().getResources(packageName.replaceAll(".", "/"));
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if(url != null) {
                    String protocol = url.getProtocol();
                    if(protocol.equals("file")) {
                        String packagePath = url.getPath().replaceAll("%20", " ");
                        packagePath = packagePath + packageName.replace(".", "/");
                        addClass(classSet, packagePath, packageName);
                    } else if(protocol.equals("jar")) {
//                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
//                        if(jarURLConnection != null) {
//                            JarFile jarFile = jarURLConnection.getJarFile();
//                            if(jarFile != null) {
//                                Enumeration<JarEntry> entries = jarFile.entries();
//                                while (entries.hasMoreElements()) {
//                                    JarEntry jarEntry = entries.nextElement();
//                                    String jarEntryname = jarEntry.getName();
//                                    if(jarEntryname.endsWith(".class")) {
//                                        String className = jarEntryname.substring(0, jarEntryname.lastIndexOf("."))
//                                                .replaceAll("/", ".");
//                                        doAddClass(classSet, className);
//                                    }
//                                }
//                            }
//                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {

        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."))
                        .replaceAll("/", ".");
                if(StringUtils.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if(StringUtils.isNotEmpty(packageName)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if(StringUtils.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }

        }

    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }


}
