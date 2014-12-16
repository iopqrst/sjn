package com.sjn.model;

import com.jfinal.plugin.activerecord.Model;
import com.sjn.thread.ParamInit;
import com.sjn.utils.EhcacheFactoryUtils;

public class Timeline extends Model<Timeline> {

	private static final long serialVersionUID = 1L;
	
	public static final Timeline dao = new Timeline();
	
	/**
	 * 根据time实际值获取
	 * @param val
	 * @return
	 */
	public static String getTimePolt(double val) {
		Object obj = EhcacheFactoryUtils.getInstance().get(
				EhcacheFactoryUtils.cache_name_system,
				ParamInit.cacheStart_timeline + val);
		return null == obj ? "" : ((Timeline) obj).getStr("timePolt");
	}

}
