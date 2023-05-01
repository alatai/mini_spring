package com.alatai.mini.bean.factory.config;

import com.alatai.mini.bean.factory.ListableBeanFactory;

/**
 * AutowireCapableBeanFactory, ListableBeanFactory 和
 * ConfigurableBeanFactory 合并在一起
 *
 * @author alatai
 * @version 1.0
 * @date 2023/05/01 20:20
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory,
		AutowireCapableBeanFactory, ConfigurableBeanFactory {
}
