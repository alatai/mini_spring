package com.alatai.mini.context;

import java.util.EventObject;

/**
 * 应用发布事件
 * 继承 Java 工具包内的 EventObject，在 Java 的时间监听继承上进行简单的封装
 *
 * @author alatai
 * @version 1.0
 * @date 2023/03/19 14:39
 */
public class ApplicationEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
