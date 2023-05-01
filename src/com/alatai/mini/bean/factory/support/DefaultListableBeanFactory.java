package com.alatai.mini.bean.factory.support;

import com.alatai.mini.bean.BeanException;
import com.alatai.mini.bean.factory.BeanDefinition;
import com.alatai.mini.bean.factory.config.AbstractAutowireCapableBeanFactory;
import com.alatai.mini.bean.factory.config.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alatai
 * @version 1.0
 * @date 2023/05/01 21:07
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {

	@Override
	public int getBeanDefinitionCount() {
		return this.beanDefinitionMap.size();
	}

	@Override
	public String[] getBeanDefinitionNames() {
		return (String[]) this.beanDefinitionNames.toArray();
	}

	@Override
	public String[] getBeanNamesForType(Class<?> type) {
		List<Object> result = new ArrayList<>();

		for (String beanDefinitionName : this.beanDefinitionNames) {
			boolean matchFound = false;
			BeanDefinition beanDefinition = this.getBeanDefinition(beanDefinitionName);
			Class<?> clz = beanDefinition.getClass();

			// isAssignableFrom：判断两个类之间的关联关系，即一个类是否可以被强制转换为另一个实例对象
			if (type.isAssignableFrom(clz)) {
				matchFound = true;
			} else {
				matchFound = false;
			}

			if (matchFound) {
				result.add(beanDefinitionName);
			}
		}

		return (String[]) result.toArray();
	}

	@Override
	public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeanException {
		String[] beanNames = getBeanNamesForType(type);
		Map<String, T> result = new LinkedHashMap<>(beanNames.length);

		for (String beanName : beanNames) {
			Object beanInstance = getBean(beanName);
			result.put(beanName, (T) beanInstance);
		}

		return result;
	}
}
