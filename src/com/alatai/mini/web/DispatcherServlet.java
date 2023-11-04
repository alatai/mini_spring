package com.alatai.mini.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * MVC核心启动类
 * 实现 web.xml 中的配置，完成 URL 映射
 * MVC的基本思路是屏蔽Servlet的概念，让程序员主要写业务逻辑代码
 * 浏览器访问URL通过映射机制找到实际的业务逻辑方法
 *
 * @author saihou
 * @version 1.0
 * @date 2023/10/04 23:58
 */
public class DispatcherServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;
    private String sContextConfigLocation;

    /**
     * 用于存储需要扫描的package列表
     */
    private List<String> packageNames = new ArrayList<>();

    /**
     * 用于存储controller的名称与对象的映射关系
     */
    private Map<String, Object> controllerObjs = new HashMap<>();

    /**
     * 用于存储controller名称数组列表
     */
    private List<String> controllerNames = new ArrayList<>();

    /**
     * 用于存储controller名称与类的映射关系
     */
    private Map<String, Class<?>> controllerClasses = new HashMap<>();

    /**
     * 保存自定义RequestMapping注解名称的列表
     */
    private List<String> urlMappingNames = new ArrayList<>();

    private Map<String, Method> mappingMethods = new HashMap<>();

    /**
     * URL对应的类
     */
    private Map<String, Class<?>> mappingClz = new HashMap<>();

    /**
     * URL对应的方法
     */
    private Map<String, Object> mappingObjs = new HashMap<>();

    /**
     * Servlet初始化方法
     * 处理从外部传入的资源，将XML文件内容解析后存入mappingValues中
     * 最后调用refresh()方法创建bean
     *
     * @param config ServletConfig
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;

        try {
            xmlPath = this.getServletContext().getResource(contextConfigLocation);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        List<String> nodeValue = XmlScanComponentHelper.getNodeValue(xmlPath);
        refresh();
    }

    /**
     * 对所有mappingValues中注册的类进行实例化，默认构造函数
     */
    protected void refresh() {
        initController();
        initMapping();
    }

    private void initController() {
        this.controllerNames = scanPackages(this.packageNames);
    }

    private List<String> scanPackages(List<String> packageNames) {
        List<String> tempControllerNames = new ArrayList<>();
        for (String packageName : packageNames) {
            tempControllerNames.addAll(scanPackage(packageName));
        }

        return tempControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        List<String> tempControllerNames = new ArrayList<>();
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replace("\\.", "/"));
        assert url != null;
        File dir = new File(url.getFile());

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                scanPackage(packageName + "." + file.getName());
            } else {
                String controllerName = packageName + "." + file.getName().replace(".class", "");
                tempControllerNames.add(controllerName);
            }

            return tempControllerNames;
        }

        return tempControllerNames;
    }

    private void initMapping() {
        for (String controllerName : this.controllerNames) {
            Class<?> clazz = this.controllerClasses.get(controllerName);
            Object obj = this.controllerObjs.get(controllerName);
            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods) {
                boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);

                if (isRequestMapping) {
                    String methodName = method.getName();
                    String urlMapping = method.getAnnotation(RequestMapping.class).value();
                    this.urlMappingNames.add(urlMapping);
                    this.mappingObjs.put(urlMapping, obj);
                    this.mappingMethods.put(urlMapping, method);
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sPath = request.getServletPath();
        if (!this.urlMappingNames.contains(sPath))
            return;

        Object obj = null;
        Object objResult = null;
        Method method = this.mappingMethods.get(sPath);
        obj = this.mappingObjs.get(sPath);

        try {
            objResult = method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().append(objResult.toString());
    }
}
