package com.alatai.test.service.impl;

import com.alatai.test.service.AService;

/**
 * 测试依赖关系
 *
 * @author alatai
 * @version 1.0
 * @date 2023/04/19 21:23
 */
public class BaseBaseService {

	private AServiceImpl as;

	public AServiceImpl getAs() {
		return as;
	}

	public void setAs(AServiceImpl as) {
		this.as = as;
	}

	public void sayHello() {
		System.out.println("Base Base Service says hello~");
	}
}
