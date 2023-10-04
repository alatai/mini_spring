package com.alatai.mini.test;

import com.alatai.mini.bean.BeanException;
import com.alatai.mini.context.ClassPathXmlApplicationContext;
import com.alatai.mini.test.service.AService;

/**
 * @author alatai
 * @version 1.0
 * @date 2023/03/17 1:39
 */
public class Main {

    public static void main(String[] args) throws BeanException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("bean.xml");
        AService aservice = (AService) ctx.getBean("aservice");
        aservice.hello();
    }
}
