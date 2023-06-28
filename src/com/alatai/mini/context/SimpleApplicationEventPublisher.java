package com.alatai.mini.context;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现最简单的事件发布者
 *
 * @author alatai
 * @version 1.0
 * @date 2023/06/28 23:25
 */
public class SimpleApplicationEventPublisher implements ApplicationEventPublisher {

	List<ApplicationListener> listeners = new ArrayList<>();

	@Override
	public void publishEvent(ApplicationEvent event) {
		for (ApplicationListener listener : listeners) {
			listener.onApplicationEvent(event);
		}
	}

	@Override
	public void addApplicationListener(ApplicationListener listener) {
		this.listeners.add(listener);
	}
}
