package com.alatai.test.service.impl;

import com.alatai.test.service.AService;

/**
 * @author alatai
 * @version 1.0
 * @date 2023/04/19 21:45
 */
public class BaseService {

	private BaseBaseService bbs;

	public BaseBaseService getBbs() {
		return bbs;
	}

	public void setBbs(BaseBaseService bbs) {
		this.bbs = bbs;
	}

	public void sayHello() {
		System.out.println("Base Service says hello~");
	}
}
