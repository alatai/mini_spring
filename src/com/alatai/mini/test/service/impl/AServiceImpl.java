package com.alatai.mini.test.service.impl;

import com.alatai.mini.test.service.AService;

/**
 * @author alatai
 * @version 1.0
 * @date 2023/03/17 1:38
 */
public class AServiceImpl implements AService {

    private String name;
    private int level;
    private String property1;
    private String property2;
    private BaseService ref1;

    public AServiceImpl() {

    }

    public AServiceImpl(String name, int level) {
        this.name = name;
        this.level = level;
    }

    @Override
    public void hello() {
        System.out.println("Hello Spring~");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public BaseService getRef1() {
        return ref1;
    }

    public void setRef1(BaseService ref1) {
        this.ref1 = ref1;
    }
}
