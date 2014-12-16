package com.sjn.handler;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jfinal.handler.Handler;
import com.sjn.config.CommonConfig;
import com.sjn.constant.Constant;
import com.sjn.utils.WebUtils;

/**
 * 全局Handler，设置一些通用功能
 * 
 * @author hzq
 * 
 */
public class GlobalHandler extends Handler {

	private static Logger log = Logger.getLogger(GlobalHandler.class);

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {

		Map<String, Cookie> cookieMap = WebUtils.readCookieMap(request);
		request.setAttribute("cookieMap", cookieMap);
		request.setAttribute("jsVersion", CommonConfig.getParamMapValue(Constant.js_version_key));
		
		nextHandler.handle(target, request, response, isHandled);

	}

}
