package com.alatai.mini.bean.factory.config;

import com.alatai.mini.bean.factory.BeanFactory;

/**
 * 维护 Bean 之间的依赖关系以及支持 Bean 处理器
 *
 * @author alatai
 * @version 1.0
 * @date 2023/05/01 20:17
 */
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {

	String SCOPE_SINGLETON = "singleton";

	String SCOPE_PROTOTYPE = "prototype";

	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

	int getBeanPostProcessorCount();

	void registerDependentBean(String beanName, String dependentBeanName);

	String[] getDependentBeans(String beanName);

	String[] getDependenciesForBean(String beanName);
}
