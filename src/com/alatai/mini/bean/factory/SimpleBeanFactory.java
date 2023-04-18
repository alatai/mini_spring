package com.alatai.mini.bean.factory;

import com.alatai.mini.bean.*;
import com.alatai.mini.bean.factory.support.BeanDefinitionRegistry;
import com.alatai.mini.bean.factory.support.DefaultSingletonBeanRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public SimpleBeanFactory() {
    }

    /**
     * 创建 Bean 实例，并存储到 singletons 中
     */
    @Override
    public Object getBean(String beanName) throws BeanException {
        Object singleton = this.getSingleton(beanName);

        if (singleton == null) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            singleton = createBean(beanDefinition);
            this.registerBean(beanName, singleton);

            if (beanDefinition.getInitMethodName() != null) {
                // init method
            }
        }

        if (singleton == null) {
            throw new BeanException("bean is null.");
        }

        return singleton;
    }

    /**
     * 利用反射创建实例
     */
    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> constructor;

        // 设置构造器参数
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

        // 设置属性值
        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pName = propertyValue.getName();
                String pType = propertyValue.getType();
                Object pValue = propertyValue.getValue();

                Class<?>[] paramTypes = new Class<?>[1];

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

                Object[] paramValues = new Object[1];
                paramValues[0] = pValue;

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

        return obj;
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
