package com.alatai.mini.bean.factory.config;

import com.alatai.mini.bean.BeanException;
import com.alatai.mini.bean.factory.BeanFactory;

/**
 * Autowired 注入用
 *
 * @author alatai
 * @version 1.0
 * @date 2023/04/24 22:57
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

	int AUTOWIRE_NO = 0;

	int AUTOWIRE_BY_NAME = 1;

	int AUTOWIRE_BY_TYPE = 2;

	Object applyBeanPostProcessorBeforeInitialization(Object existingBean,
	                                                  String beanName) throws BeanException;

	Object applyBeanPostProcessorAfterInitialization(Object existingBean,
	                                                 String beanName) throws BeanException;
}
