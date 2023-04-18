package com.alatai.mini.bean.factory.support;

import com.alatai.mini.bean.factory.BeanDefinition;

/**
 * 存放 BeanDefinition 的仓库，可以存放、移除、获取及判断 BeanDefinition 对象
 *
 * @author alatai
 * @version 1.0
 * @date 2023/04/18 14:25
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String name, BeanDefinition beanDefinition);

    void removeBeanDefinition(String name);

    BeanDefinition getBeanDefinition(String name);

    boolean containsBeanDefinition(String name);
}
