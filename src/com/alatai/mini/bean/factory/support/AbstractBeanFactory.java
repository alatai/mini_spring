package com.alatai.mini.bean.factory.support;

import com.alatai.mini.bean.BeanException;
import com.alatai.mini.bean.PropertyValue;
import com.alatai.mini.bean.PropertyValues;
import com.alatai.mini.bean.factory.BeanDefinition;
import com.alatai.mini.bean.factory.config.ConfigurableBeanFactory;
import com.alatai.mini.bean.factory.config.ConstructorArgumentValue;
import com.alatai.mini.bean.factory.config.ConstructorArgumentValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alatai
 * @version 1.0
 * @date 2023/04/24 23:16
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry
		implements ConfigurableBeanFactory, BeanDefinitionRegistry {

	protected final Map<String, BeanDefinition> beanDefinitionMap =
			new ConcurrentHashMap<>(256);
	protected final List<String> beanDefinitionNames = new ArrayList<>();
	// 存放早期实例（解决循环依赖问题）
	protected final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

	/**
	 * 对所有的 Bean 调用一次 getBean，利用其中的 createBean 创建 Bean 实例
	 */
	public void refresh() {
		for (String beanDefinitionName : beanDefinitionNames) {
			try {
				getBean(beanDefinitionName);
			} catch (BeanException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 创建 Bean 实例，并存储到 singletons 中
	 */
	@Override
	public Object getBean(String beanName) throws BeanException {
		// 先直接从容器中获取实例
		Object singleton = this.getSingleton(beanName);

		if (singleton == null) {
			// 尝试获取早期实例（毛坯实例）
			singleton = earlySingletonObjects.get(beanName);

			// 若不存在毛坯实例，则创建 bean 并注册
			if (singleton == null) {
				BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
				singleton = createBean(beanDefinition);
				this.registerBean(beanName, singleton);

				// 进行 beanPostProcessor处理
				// 1.postProcessorBeforeInitialization
				applyBeanPostProcessorBeforeInitialization(singleton, beanName);

				// 2.init method
				if (beanDefinition.getInitMethodName() != null) {
					invokeInitMethod(beanDefinition, singleton);
				}

				// 3.postProcessorAfterInitialization
				applyBeanPostProcessorAfterInitialization(singleton, beanName);
			}
		}

		if (singleton == null) {
			throw new BeanException("bean is null.");
		}

		return singleton;
	}

	private void invokeInitMethod(BeanDefinition beanDefinition, Object obj) {
		try {
			Class<?> clz = beanDefinition.getClass();
			Method method = clz.getMethod(beanDefinition.getInitMethodName());
			method.invoke(obj);
		} catch (NoSuchMethodException | IllegalAccessException |
		         InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private Object createBean(BeanDefinition beanDefinition) {
		Class<?> clz;
		// 创建毛坯实例
		Object obj = doCreateBean(beanDefinition);
		// 存放到早期实例缓存
		this.earlySingletonObjects.put(beanDefinition.getId(), obj);

		try {
			clz = Class.forName(beanDefinition.getClassName());
			handleProperties(beanDefinition, clz, obj);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		return obj;
	}

	/**
	 * 利用反射创建实例
	 */
	private Object doCreateBean(BeanDefinition beanDefinition) {
		Class<?> clz;
		Object obj = null;
		Constructor<?> constructor;

		try {
			clz = Class.forName(beanDefinition.getClassName());
			ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();

			if (!constructorArgumentValues.isEmpty()) {
				Class<?>[] paramTypes = new Class<?>[constructorArgumentValues.getArgumentCount()];
				Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];

				for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
					ConstructorArgumentValue constructorArgumentValue = constructorArgumentValues.getIndexedArgumentValue(i);

					if (Objects.equals(constructorArgumentValue.getType(), "String")
							|| Objects.equals(constructorArgumentValue.getType(), "java.lang.String")) {
						paramTypes[i] = String.class;
						paramValues[i] = constructorArgumentValue.getValue();
					} else if (Objects.equals(constructorArgumentValue.getType(), "Integer")
							|| Objects.equals(constructorArgumentValue.getType(), "java.lang.Integer")) {
						paramTypes[i] = Integer.class;
						paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
					} else if (Objects.equals(constructorArgumentValue.getType(), "int")) {
						paramTypes[i] = int.class;
						paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
					} else {
						paramTypes[i] = String.class;
						paramValues[i] = constructorArgumentValue.getValue();
					}
				}

				constructor = clz.getConstructor(paramTypes);
				obj = constructor.newInstance(paramValues);
			} else {
				obj = clz.getConstructor().newInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;
	}

	/**
	 * 设置实例属性
	 */
	private void handleProperties(BeanDefinition beanDefinition,
	                              Class<?> clz, Object obj) {
		PropertyValues propertyValues = beanDefinition.getPropertyValues();

		if (!propertyValues.isEmpty()) {
			for (int i = 0; i < propertyValues.size(); i++) {
				PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
				String pName = propertyValue.getName();
				String pType = propertyValue.getType();
				Object pValue = propertyValue.getValue();
				boolean isRef = propertyValue.isRef();

				Class<?>[] paramTypes = new Class<?>[1];
				Object[] paramValues = new Object[1];

				// 不存在引用关系属性
				if (!isRef) {
					if (Objects.equals(pType, "String")
							|| Objects.equals(pType, "java.lang.String")) {
						paramTypes[0] = String.class;
					} else if (Objects.equals(pType, "Integer")
							|| Objects.equals(pType, "java.lang.Integer")) {
						paramTypes[0] = Integer.class;
					} else if (Objects.equals(pType, "int")) {
						paramTypes[0] = int.class;
					} else {
						paramTypes[0] = String.class;
					}
					paramValues[0] = pValue;
				} else { // 存在引用关系属性
					try {
						paramTypes[0] = Class.forName(pType);
						paramValues[0] = getBean((String) pValue);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				String methodName = "set" + pName.substring(0, 1).toUpperCase()
						+ pName.substring(1);
				Method method;

				try {
					method = clz.getMethod(methodName, paramTypes);
					method.invoke(obj, paramValues);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	abstract public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeanException;

	abstract public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeanException;

	/**
	 * 注册 BeanDefinition
	 */
	@Override
	public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
		this.beanDefinitionMap.put(name, beanDefinition);
		this.beanDefinitionNames.add(name);

		if (beanDefinition.isLazyInit()) {
			try {
				getBean(name);
			} catch (BeanException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean containsBean(String beanName) {
		return super.containsSingleton(beanName);
	}

	@Override
	public boolean isSingleton(String name) {
		return this.beanDefinitionMap.get(name).isSingleton();
	}

	@Override
	public boolean isPrototype(String name) {
		return this.beanDefinitionMap.get(name).isPrototype();
	}

	@Override
	public Class<?> getType(String name) {
		return this.beanDefinitionMap.get(name).getClass();
	}

	public void registerBean(String beanName, Object obj) {
		this.registerSingleton(beanName, obj);
	}

	@Override
	public void removeBeanDefinition(String name) {
		this.beanDefinitionMap.remove(name);
		this.beanDefinitionNames.remove(name);
		this.removeSingleton(name);
	}

	@Override
	public BeanDefinition getBeanDefinition(String name) {
		return this.beanDefinitionMap.get(name);
	}

	@Override
	public boolean containsBeanDefinition(String name) {
		return this.beanDefinitionMap.containsKey(name);
	}
}
