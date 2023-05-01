package com.alatai.mini.bean.factory;

import com.alatai.mini.bean.BeanException;

import java.util.Map;

/**
 * 拓展 BeanFactory
 *
 * @author alatai
 * @version 1.0
 * @date 2023/05/01 20:01
 */
public interface ListableBeanFactory extends BeanFactory{

	boolean containsBeanDefinition(String beanName);

	int getBeanDefinitionCount();

	String[] getBeanDefinitionNames();

	String[] getBeanNamesForType(Class<?> type);

	<T> Map<String, T> getBeansOfType(Class<T> type) throws BeanException;
}
