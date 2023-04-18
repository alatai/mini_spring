package com.alatai.mini.bean.factory.config;

/**
 * 存储单例 bean
 *
 * @author alatai
 * @version 1.0
 * @date 2023/03/19 13:53
 */
public interface SingletonBeanRegistry {

    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

    boolean containsSingleton(String beanName);

    String[] getSingletonNames();
}
