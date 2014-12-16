package com.sjn.interceptor;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

/**
 * 参数封装拦截器
 * @author hzq
 *
 */
public class ParamPkgInterceptor implements Interceptor {

	private static Logger log = Logger.getLogger(ParamPkgInterceptor.class);

	public void intercept(ActionInvocation ai) {

		ai.invoke();

	}

}