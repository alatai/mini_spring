package com.alatai.mini.bean.factory.support;

import com.alatai.mini.bean.factory.config.SingletonBeanRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储单例 bean，默认实现
 *
 * @author alatai
 * @version 1.0
 * @date 2023/03/19 13:56
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    // 存放所有 bean 名称
    protected List<String> beanNames = new ArrayList<>();

    // 存放所有 bean 实例
    protected final Map<String, Object> singletonObjects =
            new ConcurrentHashMap<>(256);

    protected Map<String, Set<String>> dependentBeanMap =
            new ConcurrentHashMap<>(64);

    protected Map<String, Set<String>> dependenciesForBeanMap =
            new ConcurrentHashMap<>(64);

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        // 确保多并发时，仍然能安全地实现对单例 bean 的管理
        // 无论是单线程还是多线程，整个系统里面这个 bean 总是唯一的、单例的
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.beanNames.add(beanName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return (String[]) this.beanNames.toArray();
    }

    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.beanNames.remove(beanName);
            this.singletonObjects.remove(beanName);
        }
    }

    protected boolean hasDependentBean(String beanName) {
        return false;
    }

    protected String[] getDependentBeans(String beanName) {
        return null;
    }

    protected String[] getDependenciesForBean(String beanName) {
        return null;
    }
}
