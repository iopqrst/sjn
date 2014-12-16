package com.sjn.utils.query;

import java.io.Serializable;

/**
 * 分页封装
 * 
 * @author 董华健
 */
public class PageObject implements Serializable {

	private static final long serialVersionUID = -7914983945613661637L;

	public static final int default_pageNumber = 1;// 第几页
	public static final int default_pageSize = 20;// 每页显示几多

	protected String orderColunm;// 排序条件
	protected String orderMode;// 排序方式
	protected int pageNo = default_pageNumber;// 第几页
	protected int pageSize = default_pageSize;// 每页显示几多

	public String getOrderColunm() {
		return orderColunm;
	}

	public void setOrderColunm(String orderColunm) {
		this.orderColunm = orderColunm;
	}

	public String getOrderMode() {
		return orderMode;
	}

	public void setOrderMode(String orderMode) {
		this.orderMode = orderMode;
	}

	public int getPageNo() {
		if (pageNo <= 0) {
			pageNo = default_pageNumber;
		}
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		if (pageSize <= 0) {
			pageSize = default_pageSize;
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
