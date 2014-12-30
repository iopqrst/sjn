package com.sjn.controller;

import com.jfinal.core.Controller;
import com.sjn.model.Winner;
import com.sjn.utils.ContextUtils;
import com.sjn.utils.DESUtil;
import com.sjn.utils.StringUtils;
import com.sjn.utils.query.PageObject;

public class BaseController extends Controller {

	/**
	 * 请求/WEB-INF/下的视图文件
	 */
	public void toUrl() {
		String toUrl = getPara("toUrl");
		render(toUrl);
	}
	
	/**
	 * 获取登陆用户信息
	 * @return
	 */
	protected Winner getLoginUser() {
		return ContextUtils.getCurrentUser(getRequest(), getResponse());
	}
	
	protected String getLoginUid() {
		if(null != getLoginUser()){
			return getLoginUser().getStr("uid");
		} else {
			return null;
		}
	}
	
	protected PageObject getPageObject() {
		String pageSize = this.getPara("pageSize");
		String pageNo = this.getPara("pageNo");
		String orderColumn = this.getPara("orderColumn");
		String orderMode = this.getPara("orderMode");
		
		PageObject po = new PageObject();
		
		if(!StringUtils.isEmpty(pageSize)) {
			try {
				po.setPageSize(Integer.parseInt(pageSize));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(!StringUtils.isEmpty(pageNo)) {
			try {
				po.setPageNo(Integer.parseInt(pageNo));
				
				System.out.println(pageNo + "------------");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if(!StringUtils.isEmpty(orderColumn)) {
			po.setOrderColunm(orderColumn);
		}
		
		if(!StringUtils.isEmpty(orderColumn)) {
			po.setOrderMode(orderMode);
		}
		
		return po;
	}
	
	/**
	 * 解密字符串（非自定义key)
	 * 
	 * @param str
	 *            加密的字符串
	 * @return 解密后的字符串
	 */
	protected String decrypt(String str) {
		if (!StringUtils.isEmpty(str)) {
			DESUtil des = null;
			String result = null;
			try {
				des = new DESUtil();
				result = des.decrypt(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		return null;
	}
	
	/**
	 * 加密字符串（非自定义key)
	 * 
	 * @param str
	 *            加密的字符串
	 */
	protected String encrypt(String str) {
		if (!StringUtils.isEmpty(str)) {
			DESUtil des = null;
			String result = null;
			try {
				des = new DESUtil();
				result = des.encrypt(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		return null;
	}
}
