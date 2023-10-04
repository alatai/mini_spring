package com.alatai.mini.test.service.impl;

import com.alatai.mini.bean.factory.annotation.Autowired;

/**
 * @author alatai
 * @version 1.0
 * @date 2023/04/19 21:45
 */
public class BaseService {

	@Autowired
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
