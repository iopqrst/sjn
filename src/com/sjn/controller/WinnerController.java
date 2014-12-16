package com.sjn.controller;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.sjn.model.WinnerTimeline;
import com.sjn.service.WinnerSettingsService;

public class WinnerController extends BaseController {

	WinnerSettingsService settingService = new WinnerSettingsService();

	public void index() {
		
	}

	/**
	 * 用户自己定义时间轴的开始时间与结束时间
	 */
	public void timeline() {
		WinnerTimeline wtl = settingService.querySelfTime(getLoginUid());
		if (null != wtl) {
			JSONObject jo = new JSONObject();
			jo.put("beginPolt", wtl.getDouble("beginPolt"));
			jo.put("endPolt", wtl.getDouble("endPolt"));
			jo.put("id", this.encrypt(wtl.getInt("id") + ""));
			renderJson(jo.toString());
		} else {
			renderJson("{}");
		}
	}

	/**
	 * 保存用户设置的timeline信息
	 */
	@Before(POST.class)
	public void stimeline() {
		String stlid = this.decrypt(getPara("id"));
		String beginPolt = this.getPara("beginPolt");
		String endPolt = this.getPara("endPolt");

		if (StringUtils.isEmpty(beginPolt) || StringUtils.isEmpty(endPolt)) {
			renderJson("status", "fail");
			return;
		}

		WinnerTimeline wtl = new WinnerTimeline();
		wtl.set("uid", getLoginUid());
		wtl.set("beginPolt", beginPolt);
		wtl.set("endPolt", endPolt);

		if (!StringUtils.isEmpty(stlid)) {
			try {
				wtl.set("id", Integer.parseInt(stlid));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				renderJson("status", "fail");
				return;
			}
		} 
		
		settingService.saveOrUpdate(wtl);

		renderJson("status", "succ");
	}

}
