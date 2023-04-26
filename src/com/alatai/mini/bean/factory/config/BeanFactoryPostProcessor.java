package com.alatai.mini.bean.factory.config;

import com.alatai.mini.bean.BeanException;
import com.alatai.mini.bean.factory.BeanFactory;

/**
 * @author alatai
 * @version 1.0
 * @date 2023/04/25 14:20
 */
public interface BeanFactoryPostProcessor {

	void postProcessBeanFactory(BeanFactory beanFactory) throws BeanException;
}
