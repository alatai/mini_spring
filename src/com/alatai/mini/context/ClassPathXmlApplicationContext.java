package com.alatai.mini.context;

import com.alatai.mini.bean.BeanException;
import com.alatai.mini.bean.factory.BeanFactory;
import com.alatai.mini.bean.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.alatai.mini.bean.factory.config.AutowireCapableBeanFactory;
import com.alatai.mini.bean.factory.config.BeanFactoryPostProcessor;
import com.alatai.mini.bean.factory.xml.XmlBeanDefinitionReader;
import com.alatai.mini.core.ClassPathXmlResource;
import com.alatai.mini.core.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析指定路径下的 XML 构建应用上下文
 *
 * @author alatai
 * @version 1.0
 * @date 2023/03/17 1:13
 */
public class ClassPathXmlApplicationContext implements BeanFactory {

	private final AutowireCapableBeanFactory beanFactory;
	private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

	/**
	 * context 负责整合容器的启动过程
	 * 读取外部配置，解析 bean 定义，创建 beanFactory
	 */
	public ClassPathXmlApplicationContext(String filename) {
		this(filename, true);
	}

	public ClassPathXmlApplicationContext(String filename, boolean isRefresh) {
		Resource resource = new ClassPathXmlResource(filename);
		AutowireCapableBeanFactory autowireCapableBeanFactory = new AutowireCapableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(autowireCapableBeanFactory);
		reader.loadBeanDefinitions(resource);
		this.beanFactory = autowireCapableBeanFactory;

		if (isRefresh) {
			try {
				refresh();
			} catch (BeanException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Object getBean(String beanName) throws BeanException {
		return this.beanFactory.getBean(beanName);
	}

	@Override
	public boolean containsBean(String name) {
		return this.beanFactory.containsBean(name);
	}

	public void registerBean(String beanName, Object obj) {
		this.beanFactory.registerBean(beanName, obj);
	}

	@Override
	public boolean isSingleton(String name) {
		return false;
	}

	@Override
	public boolean isPrototype(String name) {
		return false;
	}

	@Override
	public Class<?> getType(String name) {
		return null;
	}

	public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
		return this.beanFactoryPostProcessors;
	}

	public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
		this.beanFactoryPostProcessors.add(postProcessor);
	}

	public void refresh() throws BeanException, IllegalStateException {
		registerBeanPostProcessors(this.beanFactory);
		onRefresh();
	}

	private void registerBeanPostProcessors(AutowireCapableBeanFactory beanFactory) {
		beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
	}

	private void onRefresh() {
		this.beanFactory.refresh();
	}
}
