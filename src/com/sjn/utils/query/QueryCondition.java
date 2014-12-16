package com.sjn.utils.query;

import java.util.Date;

/**
 * 封装的查询条件，用来保存除model属性以外的属性值
 * 
 * @author hzq
 *
 */
public class QueryCondition {

	private Date beginTime;
	private Date endTime;

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
