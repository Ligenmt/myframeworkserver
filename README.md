# myframeworkserver
仿Springmvc的web框架

# 基本配置
resourses目录下添加applicationContext.properties配置文件  
web.xml配置DispatchServlet类
```
<!DOCTYPE web-app PUBLIC
        "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
        "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
    <display-name>Demo Web Application</display-name>
    <servlet>
        <servlet-name>DServlet</servlet-name>
        <servlet-class>com.ligen.framework.DispatchServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>DServlet</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
</web-app>
```
配置文件：
scan.package.base: 扫描起始包名
