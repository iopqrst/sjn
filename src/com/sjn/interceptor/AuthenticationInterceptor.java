package com.sjn.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.sjn.controller.BaseController;
import com.sjn.model.Winner;
import com.sjn.utils.ContextUtils;

/**
 * 权限认证拦截器
 * 
 */
public class AuthenticationInterceptor implements Interceptor {

	private static Logger log = Logger
			.getLogger(AuthenticationInterceptor.class);

	public void intercept(ActionInvocation ai) {
		
		BaseController contro = (BaseController) ai.getController();
		HttpServletRequest request = contro.getRequest();
		HttpServletResponse response = contro.getResponse();
		
		Winner obj = ContextUtils.getCurrentUser(request, response);
		
		if(null == obj) {
			
			if(isAjaxRequest(request)) {
				contro.getResponse().setHeader("login_status", "timeout");
				contro.renderNull();
				return;
			} else {
				String uri = request.getRequestURI(); //uri 会把项目名称也打印出来，而指向域名后没有项目名会导致页面404
		
				if(-1 != uri.indexOf("/sjn")) {
					System.out.print("--->" + ContextUtils.getContextAllPath(request));
					uri = uri.replace("/sjn", "");
				}
				contro.redirect("/login?toUrl=" + uri);
			}
			
			//TODO 让一个处理不存在uri的情况，可以查看过那个项目怎么使用缓存的方式处理的
		} else {
			ai.invoke();
		}
	}

	/**
	 * 判断请求是否为 ajax 请求
	 * @param request
	 * @return
	 */
	private boolean isAjaxRequest(HttpServletRequest request) {
		String header = request.getHeader("X-Requested-With");
		if (header != null && "XMLHttpRequest".equalsIgnoreCase(header))
			return true;
		else
			return false;
	}
}
