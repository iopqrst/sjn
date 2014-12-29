package com.sjn.service;

import java.util.Date;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sjn.constant.Constant;
import com.sjn.model.Vcode;
import com.sjn.utils.DateUtils;
import com.sjn.utils.RandomUtils;
import com.sjn.utils.SendMessageUtil;
import com.sjn.utils.StringUtils;

public class VcodeService extends BaseService {

	private static final Logger log = Logger.getLogger(VcodeService.class);

	/**
	 * 发送保存验证码
	 * 
	 * @param mobile
	 * @return
	 */
	public String sendVcode(String mobile) {
		JSONObject jo = new JSONObject();

		if (StringUtils.isEmpty(mobile)) {
			jo.put("code", Constant.INTERFACE_PARAMAS_ERROR);
			jo.put("msg", "请输入您的手机号");
			return jo.toString();
		}

		if (StringUtils.isMobile(mobile)) {
			jo.put("code", Constant.INTERFACE_PARAMAS_ERROR);
			jo.put("msg", "请输入正确的手机号");
			return jo.toString();
		}

		// 1. 从数据库中获取该手机号用户在10分钟之内发送短信次数
		// 如果小于等于5，则可以再次发送，否则告知页面发送短信频繁

		String querySql = "select count(*) total from sjn_vcode where mobile = ? and createTime >= ?";

		Date tenMinsAgo = DateUtils.getDateByMinute(new Date(), -10); // 当前时间的十分钟之前

		Record record = Db.findFirst(querySql, new Object[] { mobile,
				tenMinsAgo });

		if (record.getLong("total") >= 2) {
			// TODO 发送太频繁了...
			jo.put("code", Constant.INTERFACE_FAIL);
			jo.put("msg", "验证码发送太频繁了，请稍后再试");
		} else {
			// TODO 获取验证码发送短信，并保存到数据库中
			String vcode = RandomUtils.getRandomNumber(6); // 获取6为随机短信验证码
			int result = -1; // 发送短信结果，失败重新提交

			int i = 0;
			while (i < 3) {
				// 调用发送短信的方法
				result = SendMessageUtil.sendVcode(mobile, vcode);

				if (0 == result) { // 短信发送成功
					// 记录结果
					if (addRecord(mobile, vcode)) {
						log.info(">>>>>>>>>>>>>>>>>>> 短信验证码发送成功:" + vcode);
					}
					break;
				}

				i++;
			}

			if (0 == result) {
				jo.put("code", Constant.INTERFACE_SUCCESS);
				jo.put("msg", "短信验证码发送成功，请注意查收您的短信");
			} else {
				jo.put("code", Constant.INTERFACE_FAIL);
				jo.put("msg", "获取短信验证码失败，请稍后再试");
			}
		}
		return jo.toString();
	}

	/**
	 * 保存验证码记录
	 * 
	 * @param mobile
	 * @param code
	 *            验证码信息
	 */
	public boolean addRecord(String mobile, String code) {
		return new Vcode().set("mobile", mobile).set("createTime", new Date())
				.set("vcode", code).save();
	}

	/**
	 * 验证用户短信校验码是否有效
	 * 
	 * @param mobile
	 * @param vcode
	 * @return
	 */
	public String validVcode(String mobile, String vcode) {
		JSONObject jo = new JSONObject();

		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(vcode)) {
			jo.put("code", Constant.INTERFACE_PARAMAS_ERROR);
			jo.put("msg", "手机号或短信校验码不能为空");
			return jo.toString();
		}

		// 这里并没有对查询用户最后一次短信校验码，为的是防止用户因为短信延迟无法收到短信
		// 试想一下用户在十分钟内获取了3次校验码，因为短信运营商的缘故，收到短信顺序被打乱，随便填写一个都能注册成功，是不是更人性一点？
		String querySql = "select count(*) total from sjn_vcode where mobile = ? and vcode = ? and createTime >= ?";

		Date tenMinsAgo = DateUtils.getDateByMinute(new Date(), -30); // 当前时间的十分钟之前
		Record record = Db.findFirst(querySql, new Object[] { mobile, vcode, 
				tenMinsAgo });

		if (record.getLong("total") >= 1) {
			jo.put("code", Constant.INTERFACE_SUCCESS);
			jo.put("msg", "验证码正确");
		} else {
			jo.put("code", Constant.INTERFACE_FAIL);
			jo.put("msg", "验证码不正确");
		}
		return jo.toString();
	}

}
