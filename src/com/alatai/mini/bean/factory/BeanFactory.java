package com.alatai.mini.bean.factory;

import com.alatai.mini.bean.BeanException;

/**
 * Bean 工厂
 *
 * @author alatai
 * @version 1.0
 * @date 2023/03/18 20:32
 */
public interface BeanFactory {

    /**
     * 获取 bean
     */
    Object getBean(String beanName) throws BeanException;

    /**
     * 注册 beanDefinition
     */
    // void registerBeanDefinition(BeanDefinition beanDefinition);

    // void registerBean(String beanName, Object obj);

    boolean containsBean(String name);

    boolean isSingleton(String name);

    boolean isPrototype(String name);

    Class<?> getType(String name);
}
