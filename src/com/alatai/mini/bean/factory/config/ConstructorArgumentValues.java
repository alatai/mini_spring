package com.alatai.mini.bean.factory.config;

import java.util.*;

/**
 * 对 ConstructorArgumentValue 添加封装、增加、获取、判断等操作方法
 *
 * @author alatai
 * @version 1.0
 * @date 2023/04/18 11:50
 */
public class ConstructorArgumentValues {

    private final List<ConstructorArgumentValue> constructorArgumentValueList = new ArrayList<>();

    public ConstructorArgumentValues() {}

    public void addArgumentValue(ConstructorArgumentValue constructorArgumentValue) {
        this.constructorArgumentValueList.add(constructorArgumentValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        return this.constructorArgumentValueList.get(index);
    }

    public int getArgumentCount() {
        return this.constructorArgumentValueList.size();
    }

    public boolean isEmpty() {
        return this.constructorArgumentValueList.isEmpty();
    }
}
