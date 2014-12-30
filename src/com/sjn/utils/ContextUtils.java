package com.sjn.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.sjn.config.CommonConfig;
import com.sjn.constant.Constant;
import com.sjn.model.Winner;
import com.sjn.thread.ParamInit;

/**
 * WEB上下文工具类
 * 
 * @author 董华健 2012-9-7 下午1:51:04
 */
public class ContextUtils {

	private static Logger log = Logger.getLogger(ContextUtils.class);

	/**
	 * 输出servlet文本内容
	 * 
	 * @author 董华健 2012-9-14 下午8:04:01
	 * @param response
	 * @param content
	 */
	public static void outPage(HttpServletResponse response, String content) {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding(StringUtils.encoding);
		// PrintWriter out = response.getWriter();
		// out.print(content);
		try {
			response.getOutputStream().write(
					content.getBytes(StringUtils.encoding));// char to byte 性能提升
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取上下文URL全路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getContextAllPath(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append(request.getScheme()).append("://")
				.append(request.getServerName()).append(":")
				.append(request.getServerPort())
				.append(request.getContextPath());
		String path = sb.toString();
		sb = null;
		return path;
	}

	/**
	 * 生成cookie令牌
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	public static String getCookieAuthToken(HttpServletRequest request,
			String uid) {

		if (StringUtils.isEmpty(uid))
			return null;

		String ips = WebUtils.getIpAddr(request);
		String userAgent = request.getHeader("User-Agent");
		long date = new Date().getTime();

		StringBuffer token = new StringBuffer();// 时间戳#USERID#USER_IP#USER_AGENT
		token.append(uid).append("@#!").append(date).append("@#!").append(ips)
				.append("@#!").append(userAgent);
		String authToken = token.toString();
		byte[] authTokenByte = null;
		try {
			authTokenByte = authToken.getBytes(StringUtils.encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String securityKey = (String) CommonConfig
				.getParamMapValue(Constant.config_securityKey_key);
		byte[] keyByte = Base64.decodeBase64(securityKey);

		// 加密
		byte[] securityByte = null;
		try {
			securityByte = ToolSecurityIDEA.encrypt(authTokenByte, keyByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String securityCookie = Base64.encodeBase64String(securityByte);

		// Base64编码
		try {
			securityCookie = StringUtils.encode(securityCookie);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return securityCookie;
	}

	/**
	 * 解密cookie令牌
	 * 
	 * @param encodeCookie
	 * @return //arr[1]：时间戳，arr[0]：USERID，arr[2]：USER_IP， arr[3]：USER_AGENT
	 */
	public static String[] decodeCookieAuthToken(String encodeCookie) {
		// Base64解码
		try {
			encodeCookie = StringUtils.decode(encodeCookie);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 解密
		byte[] securityByte = Base64.decodeBase64(encodeCookie);

		String securityKey = (String) CommonConfig
				.getParamMapValue(Constant.config_securityKey_key);
		byte[] keyByte = Base64.decodeBase64(securityKey);

		byte[] dataByte = null;
		try {
			dataByte = ToolSecurityIDEA.decrypt(securityByte, keyByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String data = new String(dataByte);

		String[] dataArr = data.split("@#!");

		return dataArr;
	}

	/**
	 * 获取登陆用户的信息
	 * 
	 * <pre>
	 * 从cookie中获取手机号，然后根据手机号获取用户的信息，其实 getCurrentUser 也可以，但是
	 * 每次都要解析一大长串，为了效率，所有改为直接使用手机号获取
	 * </pre>
	 * 
	 * @param request
	 * @return
	 */
	public static Winner getLoginUser(HttpServletRequest request) {
		
		Cookie mobileCookie = WebUtils.getCookieByName(request, "sid");
		if (null != mobileCookie && !StringUtils.isEmpty(mobileCookie.getValue())) {
			EhcacheFactoryUtils cacheFactory = EhcacheFactoryUtils
					.getInstance();
			
			try {
				DESUtil des = new DESUtil();
				String mobile = des.decrypt(mobileCookie.getValue());
				
				Object winner = cacheFactory.get(
						EhcacheFactoryUtils.cache_name_system,
						ParamInit.cacheStart_winner + mobile);
				
				if (null != winner) {
					return (Winner) winner;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取当前登录用户
	 * 
	 * @param changeCookie
	 *            是否要修改cookie的值
	 * @return
	 */
	public static Winner getCurrentUser(HttpServletRequest request,
			HttpServletResponse response, boolean changeCookie) {

		Cookie loginCookie = WebUtils.getCookieByName(request, "stoken");
		if (null != loginCookie && !StringUtils.isEmpty(loginCookie.getValue())) {
			String[] datas = ContextUtils.decodeCookieAuthToken(loginCookie
					.getValue());

			if (null == datas || datas.length != 4) {
				return null;
			}

			String uid = datas[0];// 用户id
			long loginDateTimes = Long.parseLong(datas[1]);// 时间戳
			String ip = datas[2];// ip地址
			String userAgent = datas[3];// USER_AGENT

			String newIp = WebUtils.getIpAddr(request);
			String newUserAgent = request.getHeader("User-Agent");

			Date start = new Date();
			start.setTime(loginDateTimes);

			int intervalMins = DateUtils.getMinutesSpace(start, new Date()); // 间隔分钟数
			int sessionMins = (Integer) CommonConfig
					.getParamMapValue(Constant.config_sessionMin_key);

			log.info(">>>>>>>> session mins = " + sessionMins
					+ ", intervalMins = " + intervalMins);

			if (ip.equals(newIp) && userAgent.equals(newUserAgent)
					&& intervalMins <= sessionMins) {

				EhcacheFactoryUtils cacheFactory = EhcacheFactoryUtils
						.getInstance();
				Object winner = cacheFactory.get(
						EhcacheFactoryUtils.cache_name_system,
						ParamInit.cacheStart_winner + uid);

				if (null != winner) {
					Winner w = (Winner) winner;

					if (changeCookie) {
						// 重新更新cookie中的时间
						boolean autoLogin = loginCookie.getMaxAge() > 0 ? true
								: false;
						setCurrentUser(request, response, w, autoLogin);

						log.info(">>>>>>>>>>>>>> 重新更新了cookie中的用户信息");
					}
					return w;
				}

			}
		}

		return null;
	}

	/**
	 * 设置当前登录用户
	 * 
	 * @return
	 */
	public static void setCurrentUser(HttpServletRequest request,
			HttpServletResponse response, Winner winner, boolean autoLogin) {

		int maxAgeTemp = -1;
		if (autoLogin) {
			maxAgeTemp = 3600 * 24 * 15;// 15天
		}

		try {
			DESUtil des = new DESUtil();
			String encryptMobile = des.encrypt(winner.getStr("mobile"));

			// 用户名cookie
			Cookie userName = new Cookie("sid", encryptMobile);
			userName.setMaxAge(maxAgeTemp);
			userName.setPath("/");
			response.addCookie(userName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// uid cookie
		// Cookie uidCookie = new Cookie("uid", winner.getStr("uid"));
		// uidCookie.setMaxAge(maxAgeTemp);
		// uidCookie.setPath("/");
		// response.addCookie(uidCookie);

		// 登陆认证cookie
		String authToken = ContextUtils.getCookieAuthToken(request,
				winner.getStr("uid"));
		WebUtils.addCookie(response, "stoken", authToken, maxAgeTemp);
	}

	/**
	 * 获取请求参数
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (null != value && !value.isEmpty()) {
			try {
				value = URLDecoder.decode(value, StringUtils.encoding).trim();
			} catch (UnsupportedEncodingException e) {
				log.error("decode异常：" + value);
			}
		}
		return value;
	}

	/**
	 * 请求流转字符串
	 * 
	 * @param request
	 * @return
	 */
	public static String requestStream(HttpServletRequest request) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(ServletInputStream) request.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("request.getInputStream() to String 异常");
		}
		return null;
	}
}
