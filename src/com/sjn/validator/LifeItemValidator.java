package com.sjn.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.sjn.model.LifeItem;

/**
 * WinnerValidator.
 */
public class LifeItemValidator extends Validator {
	
	protected void validate(Controller controller) {
//		validateRequiredString("winner.mobile", "mobileMsg", "请输入手机号");
//		validateRegex("winner.mobile", "1[0-9]{10}", "mobileMsg", "请输入正确的手机号");
//		validateEqualString("winner.mobile", "", "mobileMsg", "手机号已经存在");
//		validateRequiredString("winner.password", "passwordMsg", "请输入密码");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(LifeItem.class);
		
		String actionKey = getActionKey();
//		if (actionKey.equals("/reg/doReg"))
//			controller.render("reg.jsp");
//		else if (actionKey.equals("/blog/update"))
//			controller.render("edit.jsp");
	}
}
