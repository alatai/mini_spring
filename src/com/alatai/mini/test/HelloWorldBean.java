package com.alatai.mini.test;

import com.alatai.mini.web.RequestMapping;

/**
 * @author saihou
 * @version 1.0
 * @date 2023/10/30 21:03
 */
public class HelloWorldBean {

    @RequestMapping("/test")
    public String doGet() {
        return "hello world! by doGet";
    }

    public String doPost() {
        return "hello world! by doPost";
    }
}
