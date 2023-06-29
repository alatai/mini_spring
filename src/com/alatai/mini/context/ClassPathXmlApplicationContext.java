package com.alatai.mini.context;

import com.alatai.mini.bean.BeanException;
import com.alatai.mini.bean.factory.support.DefaultListableBeanFactory;
import com.alatai.mini.bean.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.alatai.mini.bean.factory.config.BeanFactoryPostProcessor;
import com.alatai.mini.bean.factory.config.ConfigurableListableBeanFactory;
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
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

	private final DefaultListableBeanFactory beanFactory;
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
		DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
		reader.loadBeanDefinitions(resource);
		this.beanFactory = defaultListableBeanFactory;

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
	public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
		return this.beanFactory;
	}

	public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
		return this.beanFactoryPostProcessors;
	}

	@Override

	public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
		this.beanFactoryPostProcessors.add(postProcessor);
	}

	@Override
	public void refresh() throws BeanException, IllegalStateException {
		registerBeanPostProcessors(this.beanFactory);
		onRefresh();
	}

	@Override
	void registerListeners() {
		ApplicationListener listener = new ApplicationListener();
		this.getApplicationEventPublisher().addApplicationListener(listener);
	}

	@Override
	void initApplicationEventPublisher() {
		ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
		this.setApplicationEventPublisher(aep);
	}

	@Override
	void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {

	}

	@Override
	public void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
		beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
	}

	@Override
	public void addApplicationListener(ApplicationListener listener) {
		this.getApplicationEventPublisher().addApplicationListener(listener);
	}

	@Override
	public void onRefresh() {
		this.beanFactory.refresh();
	}

	@Override
	void finishRefresh() {
		publishEvent(new ContextRefreshEvent("Context Refreshed..."));
	}
}
