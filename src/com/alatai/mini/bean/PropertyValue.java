package com.alatai.mini.bean;

/**
 * 字段映射
 *
 * @author alatai
 * @version 1.0
 * @date 2023/03/19 15:08
 */
public class PropertyValue {

    private final String type;
    private final String name;
    private final Object value;

    public PropertyValue(String type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
