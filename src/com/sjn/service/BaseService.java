package com.sjn.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.sjn.utils.query.PageObject;

public abstract class BaseService{
	
	protected Page<?> queryObject(PageObject pageObject ,
			StringBuffer sql, Object... paras) {
		
		if(null == sql) return null;
		
		// 排序
		String orderColunm = pageObject.getOrderColunm();
		String orderMode = pageObject.getOrderMode();

		if (null != orderColunm && !orderColunm.isEmpty() && null != orderMode
				&& !orderMode.isEmpty()) {
			sql.append(" order by ").append(orderColunm).append(" ")
					.append(orderMode);
		}

		String select = getSelectSql(sql.toString());
		String fromSql = getFromSql(sql.toString());
		
		if(null == select || null == fromSql) {
			return null;
		}

		System.out.println("-------------------pageNo----" + pageObject.getPageNo()
				+ ", pageSize = " + pageObject.getPageSize());
		
		Page<?> page = Db.paginate(pageObject.getPageNo(), pageObject
				.getPageSize(), select, fromSql, paras);
		
		return page;
	}
	
	/**
	 * 获取sql语句第一个from后的sql语句
	 * @param sql
	 * @return
	 */
	private String getFromSql(String sql) {
		if(null != sql && sql.trim().length() != 0 ) {
			sql = sql.replace("FROM", "from");
			int first = sql.indexOf("from");
			String sqlSuffix = sql.substring(first);
			
			System.out.println("---------------suffix----" + sqlSuffix);
			return sqlSuffix; 
		}
		return null;
	}
	
	/**
	 * 获取sql语句第一个from 之前的sql语句
	 * @param sql
	 * @return
	 */
	private String getSelectSql(String sql) {
		if(null != sql && sql.trim().length() != 0 ) {
			sql = sql.replace("FROM", "from");
			int first = sql.indexOf("from");
			String sqlPrefix = sql.substring(0, first);
			
			System.out.println("---------------prefix----" + sqlPrefix);
			return sqlPrefix; 
		}
		return null;
	}

}
