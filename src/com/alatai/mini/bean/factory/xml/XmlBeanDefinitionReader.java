package com.alatai.mini.bean.factory.xml;

import com.alatai.mini.bean.factory.config.ConstructorArgumentValue;
import com.alatai.mini.bean.factory.config.ConstructorArgumentValues;
import com.alatai.mini.bean.PropertyValues;
import com.alatai.mini.bean.factory.BeanDefinition;
import com.alatai.mini.bean.factory.support.AbstractBeanFactory;
import com.alatai.mini.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 解析 XML 中的 bean 定义，转换为 BeanDefinition
 *
 * @author alatai
 * @version 1.0
 * @date 2023/03/18 21:35
 */
public class XmlBeanDefinitionReader {

	private final AbstractBeanFactory abstractBeanFactory;

	public XmlBeanDefinitionReader(AbstractBeanFactory abstractBeanFactory) {
		this.abstractBeanFactory = abstractBeanFactory;
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
			List<Object> refs = new ArrayList<>();

			for (Element propertyElement : propertyElements) {
				String pType = propertyElement.attributeValue("type");
				String pName = propertyElement.attributeValue("name");
				String pValue = propertyElement.attributeValue("value");
				String pRef = propertyElement.attributeValue("ref");
				String pV = "";
				boolean isRef = false;

				if (pValue != null && !Objects.equals(pValue, "")) {
					pV = pValue;
				} else if (pRef != null && !Objects.equals(pRef, "")) {
					isRef = true;
					pV = pRef;
					refs.add(pRef);
				}

				propertyValues.addPropertyValue(pType, pName, pV, isRef);
			}

			beanDefinition.setPropertyValues(propertyValues);
			String[] refArray = refs.toArray(new String[0]);
			beanDefinition.setDependsOn(refArray);

			// 设置构造器参数
			List<Element> constructorElements = element.elements("constructor-arg");
			ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();

			for (Element constructorElement : constructorElements) {
				String aType = constructorElement.attributeValue("type");
				String aName = constructorElement.attributeValue("name");
				String aValue = constructorElement.attributeValue("value");
				constructorArgumentValues.addArgumentValue(new ConstructorArgumentValue(aType, aName, aValue));
			}

			beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
			this.abstractBeanFactory.registerBeanDefinition(beanID, beanDefinition);
		}

	}
}
