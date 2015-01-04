package com.sjn.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.ClearInterceptor;
import com.sjn.constant.Constant;
import com.sjn.service.VcodeService;
import com.sjn.service.WinnerService;
import com.sjn.utils.StringUtils;
import com.sjn.utils.WebUtils;

@ClearInterceptor
public class LoginController extends BaseController {

	WinnerService winnerService = new WinnerService();
	VcodeService codeService = new VcodeService(); //短信验证码

	public void index() {
		setAttr("hideNavLogin", true);
		render("login.jsp");
	}

	// TODO 登陆校验没有做呢
	public void login() {
		String mobile = getPara("mobile");
		String password = getPara("password");
		String remember = getPara("remember");
		
		setAttr("hideNavLogin", true);

		if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
			setAttr("error_msg", "手机号或者密码不能为空");
			setAttr("mobile", mobile);
			redirect("/login");
			return;
		}
		
		boolean autoLogin = false;

		if (null != remember && remember.equals("1")) {
			autoLogin = true;
		}

		int result = winnerService.validateWinner(getRequest(), getResponse(),
				mobile, password, autoLogin);
		if (result == Constant.login_info_success) { // 成功
			String toUrl = getPara("toUrl");
			if (!StringUtils.isEmpty(toUrl)) {
				redirect(toUrl);
			} else {
				redirect("/life");
			}
		} else {
			setAttr("mobile", mobile);
			setAttr("error_msg", "手机号或者密码错误");
			render("login.jsp");
		}
	}
	
	/**
	 * 验证手机号
	 * @return status :  1 试机号码不可用
	 * 				     2 号码可以使用
	 * 					-1 参数不正确 
	 */
	public void vmobile() {
		String mobile = this.getPara("mobile");
		JSONObject jresult = new JSONObject();
		if(!StringUtils.isEmpty(mobile)) {
			long count = winnerService.getMobileCount(mobile);
			jresult.put("status", (count > 0 ? 1 : 2));
		} else {
			jresult.put("status", "-1");
		}
		renderJson(jresult.toString());
	}
	
	/**
	 * 注销
	 */
	public void logout() {
		WebUtils.addCookie(getResponse(), "stoken", null, 0);
		WebUtils.addCookie(getResponse(), "mobile", null, 0);
		redirect("/");
	}
	
	/**
	 * 发送短信验证码，保存到数据库中
	 */
	public void sendVcode() {
		renderJson(codeService.sendVcode(getPara("mobile") , "login"));
	}
}
