package com.alatai.mini.bean.factory.xml;

import com.alatai.mini.bean.ArgumentValue;
import com.alatai.mini.bean.ArgumentValues;
import com.alatai.mini.bean.PropertyValues;
import com.alatai.mini.bean.factory.BeanDefinition;
import com.alatai.mini.bean.factory.SimpleBeanFactory;
import com.alatai.mini.core.Resource;
import org.dom4j.Element;

import java.util.List;

/**
 * 解析 XML 中的 bean 定义，转换为 BeanDefinition
 *
 * @author alatai
 * @version 1.0
 * @date 2023/03/18 21:35
 */
public class XmlBeanDefinitionReader {

    private final SimpleBeanFactory simpleBeanFactory;

    public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
        this.simpleBeanFactory = simpleBeanFactory;
    }

    /**
     * 把解析的 XML 内容转换成 BeanDefinition，并加载到 BeanFactory
     */
    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);

            // 设置属性
            List<Element> propertyElements = element.elements("property");
            PropertyValues propertyValues = new PropertyValues();

            for (Element propertyElement : propertyElements) {
                String pType = propertyElement.attributeValue("type");
                String pName = propertyElement.attributeValue("name");
                String pValue = propertyElement.attributeValue("value");
                propertyValues.addPropertyValue(pType, pName, pValue);
            }

            beanDefinition.setPropertyValues(propertyValues);

            // 设置构造器参数
            List<Element> constructorElements = element.elements("constructor-arg");
            ArgumentValues argumentValues = new ArgumentValues();

            for (Element constructorElement : constructorElements) {
                String aType = constructorElement.attributeValue("type");
                String aName = constructorElement.attributeValue("name");
                String aValue = constructorElement.attributeValue("value");
                argumentValues.addArgumentValue(new ArgumentValue(aType, aName, aValue));
            }

            beanDefinition.setConstructorArgumentValues(argumentValues);
            this.simpleBeanFactory.registerBeanDefinition(beanID, beanDefinition);
        }

    }
}
