package com.alatai.mini.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 对 PropertyValue 添加封装、增加、获取、判断等操作方法
 *
 * @author alatai
 * @version 1.0
 * @date 2023/04/18 13:27
 */
public class PropertyValues {

    private final List<PropertyValue> propertyValueList;

    public PropertyValues() {
        this.propertyValueList = new ArrayList<>(10);
    }

    public List<PropertyValue> getPropertyValueList() {
        return this.propertyValueList;
    }

    public int size() {
        return this.propertyValueList.size();
    }

    public void addPropertyValue(PropertyValue propertyValue) {
        this.propertyValueList.add(propertyValue);
    }

    public void addPropertyValue(String type, String name, Object value,
                                 boolean isRef) {
        this.addPropertyValue(new PropertyValue(type, name, value, isRef));
    }

    public void removePropertyValue(PropertyValue propertyValue) {
        this.propertyValueList.remove(propertyValue);
    }

    public void removePropertyValue(String propertyName) {
        this.propertyValueList.remove(this.getPropertyValue(propertyName));
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[this.propertyValueList.size()]);
    }

    private PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue propertyValue : this.propertyValueList) {
            if (Objects.equals(propertyValue.getName(), propertyName)) {
                return propertyValue;
            }
        }
        return null;
    }

    public Object get(String propertyName) {
        PropertyValue propertyValue = getPropertyValue(propertyName);
        return propertyValue != null ? propertyValue.getValue() : null;
    }

    public boolean contains(String propertyName) {
        return this.getPropertyValue(propertyName) != null;
    }

    public boolean isEmpty() {
        return this.propertyValueList.isEmpty();
    }
}
