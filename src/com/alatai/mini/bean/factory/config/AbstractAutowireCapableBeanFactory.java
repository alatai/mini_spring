package com.alatai.mini.bean.factory.config;

import com.alatai.mini.bean.BeanException;
import com.alatai.mini.bean.factory.support.AbstractBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alatai
 * @version 1.0
 * @date 2023/05/01 20:29
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
		implements AutowireCapableBeanFactory {

	private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		this.beanPostProcessors.remove(beanPostProcessor);
		this.beanPostProcessors.add(beanPostProcessor);
	}

	public int getBeanPostProcessorCount() {
		return this.beanPostProcessors.size();
	}

	public List<BeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}

	@Override
	public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName)
			throws BeanException {
		Object result = existingBean;
		for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
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
		for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
			beanPostProcessor.setBeanFactory(this);
			result = beanPostProcessor.postProcessAfterInitialization(result, beanName);

			if (result == null) {
				return null;
			}
		}

		return result;
	}
}
