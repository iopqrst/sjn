package com.sjn.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;

/**
 * 发送短信方法
 *
 */
public class SendMessageUtil {

	/**
	 * 发送注册短信验证码
	 * 
	 * @param mobile
	 * @return 发送结果 ： 成功 = 0； 失败=其他
	 */
	public static int sendVcode(String mobile, String code) {

		String tplValue = "";

		try {
			tplValue = URLEncoder.encode("#code#=" + code + "&#company#=时间呢",
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String result = HttpKit.post("http://yunpian.com/v1/sms/tpl_send.json",
				"apikey=52a0c8970d8e5571bdc8240a46a23218&tpl_id=2&mobile=" + mobile
						+ "&tpl_value=" + tplValue);

		System.out.println(result);
		JSONObject obj = JSON.parseObject(result);
		return obj.getIntValue("code");
	}

	public static void main(String[] args) {
		sendVcode("18612834873", "19901117");
		
//		String msg = "{\"code\":0,\"msg\":\"OK\",\"result\":{\"count\":1,\"fee\":1,\"sid\":1090852413}}";
//		
//		JSONObject obj = JSON.parseObject(msg);
//		System.out.println(obj.get("code"));
	}
}
