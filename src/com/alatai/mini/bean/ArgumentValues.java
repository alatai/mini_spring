package com.alatai.mini.bean;

import java.util.*;

/**
 * 对 ArgumentValue 添加封装、增加、获取、判断等操作方法
 *
 * @author alatai
 * @version 1.0
 * @date 2023/04/18 11:50
 */
public class ArgumentValues {

    private final List<ArgumentValue> argumentValueList = new ArrayList<>();

    public ArgumentValues() {}

    public void addArgumentValue(ArgumentValue argumentValue) {
        this.argumentValueList.add(argumentValue);
    }

    public ArgumentValue getIndexedArgumentValue(int index) {
        return this.argumentValueList.get(index);
    }

    public int getArgumentCount() {
        return this.argumentValueList.size();
    }

    public boolean isEmpty() {
        return this.argumentValueList.isEmpty();
    }
}
