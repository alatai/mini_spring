package com.alatai.mini.bean.factory.annotation;

import com.alatai.mini.bean.BeanException;
import com.alatai.mini.bean.factory.BeanFactory;
import com.alatai.mini.bean.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * Autowire 处理逻辑
 *
 * @author alatai
 * @version 1.0
 * @date 2023/04/24 22:52
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

	private BeanFactory beanFactory;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeanException {
		Class<?> clz = bean.getClass();
		Field[] fields = clz.getDeclaredFields();

		for (Field field : fields) {
			// 判断当前字段上是否标有 Autowired 注解
			boolean isAutowired = field.isAnnotationPresent(Autowired.class);

			if (isAutowired) {
				String fieldName = field.getName();
				Object autowiredObj = this.beanFactory.getBean(fieldName);
				field.setAccessible(true);
				try {
					field.set(bean, autowiredObj);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeanException {
		return null;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
}
