package com.alatai.mini.bean.factory.config;

import com.alatai.mini.bean.BeanException;
import com.alatai.mini.bean.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.alatai.mini.bean.factory.support.AbstractBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Autowired 注入用
 *
 * @author alatai
 * @version 1.0
 * @date 2023/04/24 22:57
 */
public class AutowireCapableBeanFactory extends AbstractBeanFactory {

	private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors = new ArrayList<>();

	public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor beanPostProcessor) {
		this.beanPostProcessors.remove(beanPostProcessor);
		this.beanPostProcessors.add(beanPostProcessor);
	}

	public int getBeanPostProcessorCount() {
		return this.beanPostProcessors.size();
	}

	public List<AutowiredAnnotationBeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}

	@Override
	public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName)
			throws BeanException {
		Object result = existingBean;
		for (AutowiredAnnotationBeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
			beanPostProcessor.setBeanFactory(this);
			result = beanPostProcessor.postProcessBeforeInitialization(result, beanName);

			if (result == null) {
				return null;
			}
		}

		return result;
	}

	@Override
	public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName)
			throws BeanException {
		Object result = existingBean;
		for (AutowiredAnnotationBeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
			beanPostProcessor.setBeanFactory(this);
			result = beanPostProcessor.postProcessAfterInitialization(result, beanName);

			if (result == null) {
				return null;
			}
		}

		return result;
	}
}
