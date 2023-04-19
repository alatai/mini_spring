package com.alatai.mini.context;

import com.alatai.mini.bean.BeanException;
import com.alatai.mini.bean.factory.BeanFactory;
import com.alatai.mini.bean.factory.SimpleBeanFactory;
import com.alatai.mini.bean.factory.xml.XmlBeanDefinitionReader;
import com.alatai.mini.core.ClassPathXmlResource;
import com.alatai.mini.core.Resource;

/**
 * 解析指定路径下的 XML 构建应用上下文
 *
 * @author alatai
 * @version 1.0
 * @date 2023/03/17 1:13
 */
public class ClassPathXmlApplicationContext implements BeanFactory {

	private final SimpleBeanFactory beanFactory;

	/**
	 * context 负责整合容器的启动过程
	 * 读取外部配置，解析 bean 定义，创建 beanFactory
	 */
	public ClassPathXmlApplicationContext(String filename) {
		this(filename, true);
	}

	public ClassPathXmlApplicationContext(String filename, boolean isRefresh) {
		Resource resource = new ClassPathXmlResource(filename);
		SimpleBeanFactory simpleBeanFactory = new SimpleBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(simpleBeanFactory);
		reader.loadBeanDefinitions(resource);
		this.beanFactory = simpleBeanFactory;

		if (!isRefresh) {
			this.beanFactory.refresh();
		}
	}

	@Override
	public Object getBean(String beanName) throws BeanException {
		return this.beanFactory.getBean(beanName);
	}

	@Override
	public boolean containsBean(String name) {
		return false;
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


}
