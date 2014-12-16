package com.sjn.controller;

import com.jfinal.aop.ClearInterceptor;
import com.sjn.model.Winner;
import com.sjn.service.VcodeService;
import com.sjn.service.WinnerService;
import com.sjn.utils.ContextUtils;
import com.sjn.utils.StringUtils;

@ClearInterceptor
public class RegisterController extends BaseController {

	WinnerService winnerService = new WinnerService();
	
	VcodeService codeService = new VcodeService();
	
	public void index() {
		setAttr("hideNavLogin", true);
		render("reg.jsp");
	}
	
	//@Before(RegisterValidator.class)
	public void doReg() {
		
		String mobile = getPara("mobile");
		String password = getPara("password");
		
		if(StringUtils.isMobile(mobile)) {
			setAttr("error_msg", "请输入正确的手机号码");
			redirect("/reg");
			return;
		}
		
		if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
			setAttr("error_msg", "手机号或者密码不能为空");
			setAttr("mobile", mobile);
			redirect("/reg");
			return;
		}
		
		if(winnerService.getMobileCount(mobile) > 0) {
			setAttr("error_msg", "手机号已经注册，可以直接登录");
			setAttr("mobile", mobile);
			redirect("/reg");
			return;
		}
		
		Winner w = winnerService.save(new Winner().set("mobile", mobile).set("password", password));
		
		ContextUtils.setCurrentUser(getRequest(), getResponse(), w, false);
		
		redirect("/life");
	}
	
	/**
	 * 发送短信验证码，保存到数据库中
	 */
	public void sendVcode() {
		renderJson(codeService.sendVcode(getPara("mobile")));
	}
	
}
