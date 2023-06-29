package com.alatai.mini.context;

import com.alatai.mini.bean.BeanException;
import com.alatai.mini.bean.factory.ListableBeanFactory;
import com.alatai.mini.bean.factory.config.BeanFactoryPostProcessor;
import com.alatai.mini.bean.factory.config.ConfigurableBeanFactory;
import com.alatai.mini.bean.factory.config.ConfigurableListableBeanFactory;
import com.alatai.mini.core.env.Environment;
import com.alatai.mini.core.env.EnvironmentCapable;

/**
 * 公共接口，所有上下文都实现自 ApplicationContext，支持上下文环境和事件发布
 *
 * @author alatai
 * @version 1.0
 * @date 2023/06/29 21:39
 */
public interface ApplicationContext extends EnvironmentCapable,
		ListableBeanFactory, ConfigurableBeanFactory, ApplicationEventPublisher {

	String getApplicationName();

	long getStartupDate();

	ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

	void setEnvironment(Environment environment);

	Environment getEnvironment();

	void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

	void refresh() throws BeanException, IllegalStateException;

	void close();

	boolean isActive();
}
