package com.alatai.mini.web;

import java.util.HashMap;
import java.util.Map;

/**
 * MVC核心启动类
 * 实现 web.xml 中的配置，完成 URL 映射
 *
 * @author saihou
 * @version 1.0
 * @date 2023/10/04 23:58
 */
public class DispatcherServlet {

    private Map<String, MappingValue> mappingValues;
    private Map<String, Class<?>> mappingClz = new HashMap<>();
    private Map<String, Object> mappingObjs = new HashMap<>();
}
