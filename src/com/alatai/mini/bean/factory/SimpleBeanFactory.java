package com.alatai.mini.bean.factory;

import com.alatai.mini.bean.*;
import com.alatai.mini.bean.factory.support.BeanDefinitionRegistry;
import com.alatai.mini.bean.factory.support.DefaultSingletonBeanRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanFactory 简单实现
 *
 * @author alatai
 * @version 1.0
 * @date 2023/03/18 21:47
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry
		implements BeanFactory, BeanDefinitionRegistry {

	private final Map<String, BeanDefinition> beanDefinitionMap =
			new ConcurrentHashMap<>(256);
	private final List<String> beanDefinitionNames = new ArrayList<>();
	// 存放早期实例（解决循环依赖问题）
	private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

	public SimpleBeanFactory() {
	}

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
		Object singleton = this.getSingleton(beanName);

		if (singleton == null) {
			// 尝试获取早期实例（毛坯实例）
			singleton = earlySingletonObjects.get(beanName);

			if (singleton == null) {
				BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
				singleton = createBean(beanDefinition);
				this.registerBean(beanName, singleton);

				if (beanDefinition.getInitMethodName() != null) {
					// init method
				}
			}
		}

		if (singleton == null) {
			throw new BeanException("bean is null.");
		}

		return singleton;
	}

	private Object createBean(BeanDefinition beanDefinition) {
		Class<?> clz;
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
			ArgumentValues argumentValues = beanDefinition.getConstructorArgumentValues();

			if (!argumentValues.isEmpty()) {
				Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
				Object[] paramValues = new Object[argumentValues.getArgumentCount()];

				for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
					ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);

					if (Objects.equals(argumentValue.getType(), "String")
							|| Objects.equals(argumentValue.getType(), "java.lang.String")) {
						paramTypes[i] = String.class;
						paramValues[i] = argumentValue.getValue();
					} else if (Objects.equals(argumentValue.getType(), "Integer")
							|| Objects.equals(argumentValue.getType(), "java.lang.Integer")) {
						paramTypes[i] = Integer.class;
						paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
					} else if (Objects.equals(argumentValue.getType(), "int")) {
						paramTypes[i] = int.class;
						paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
					} else {
						paramTypes[i] = String.class;
						paramValues[i] = argumentValue.getValue();
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
