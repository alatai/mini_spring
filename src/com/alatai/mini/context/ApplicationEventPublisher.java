package com.alatai.mini.context;

/**
 * 监听应用发布事件
 *
 * @author alatai
 * @version 1.0
 * @date 2023/03/19 14:38
 */
public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);

    void addApplicationListener(ApplicationListener listener);
}
