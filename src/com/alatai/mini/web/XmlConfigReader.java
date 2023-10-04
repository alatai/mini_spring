package com.alatai.mini.web;

import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * 解析配置文件
 *
 * @author saihou
 * @version 1.0
 * @date 2023/10/04 23:46
 */
public class XmlConfigReader {

    public XmlConfigReader() {
    }

    public Map<String, MappingValue> loadConfig(Resource resource) {
        Map<String, MappingValue> mappings = new HashMap<>();

        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            String beanMethod = element.attributeValue("value");

            mappings.put(beanId, new MappingValue(beanId, beanClassName, beanMethod));
        }

        return mappings;
    }
}
