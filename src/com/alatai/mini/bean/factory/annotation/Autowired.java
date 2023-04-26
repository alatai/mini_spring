package com.alatai.mini.bean.factory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Autowire 注解
 *
 * @author alatai
 * @version 1.0
 * @date 2023/04/24 22:37
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME) // 定义注解在哪一个级别可用
// RetentionPolicy.SOURCE：注解只保留在源文件，当 Java 文件编译成 .class 文件的时候，被其标注的注解被遗弃
// RetentionPolicy.CLASS：注解被保留到 class 文件中，但 JVM 加载 .class
// 文件时，被其标注的注解被遗弃，这是默认的生命周期
// RetentionPolicy.RUNTIME：注解不仅会被保留到 .class 文件中，JVM 加载 .class
// 文件之后，被其标注的注解仍然存在，所以这个时候才能通过反射机制读取注解的信息，而前两个生命周期中，通过反射机制读取不到注解信息
public @interface Autowired {
}
