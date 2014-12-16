package com.sjn.controller;

import com.jfinal.aop.ClearInterceptor;

@ClearInterceptor
public class IndexController extends BaseController {

	public void index() {
		
		//TODO 自动登录的事情等待...
		System.out.println("----------------------------------");
		System.out.println("----------------------------------");
		System.out.println("----------------------------------");
		System.out.println("----------------------------------");
		System.out.println("----------------自动登录的事情------------------");
		System.out.println("----------------------------------");
		System.out.println("----------------------------------");
		System.out.println("----------------------------------");
		System.out.println("----------------------------------");
		System.out.println("----------------------------------");
		System.out.println("----------------------------------");
		
		render("/WEB-INF/w/welcome.jsp");
	}
}
