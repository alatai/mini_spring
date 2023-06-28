package com.alatai.mini.context;

import java.util.EventListener;

/**
 * @author alatai
 * @version 1.0
 * @date 2023/06/28 23:07
 */
public class ApplicationListener implements EventListener {

	public void onApplicationEvent(ApplicationEvent event) {
		System.out.println(event.toString());
	}
}
