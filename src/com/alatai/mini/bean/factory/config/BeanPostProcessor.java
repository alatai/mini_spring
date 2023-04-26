package com.alatai.mini.bean.factory.config;

import com.alatai.mini.bean.BeanException;

/**
 * Bean 处理器 Processor，由处理器来解释注解
 *
 * @author alatai
 * @version 1.0
 * @date 2023/04/24 22:35
 */
public interface BeanPostProcessor {

	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeanException;

	Object postProcessAfterInitialization(Object bean, String beanName) throws BeanException;


}
