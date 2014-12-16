package com.sjn.service;

import com.sjn.model.WinnerTimeline;

public class WinnerSettingsService extends BaseService {

	public WinnerTimeline querySelfTime(String uid) {
		return WinnerTimeline.dao.findFirst(
				"select * from sjn_winner_timeline where uid = ?", uid);
	}
	
	public void saveOrUpdate(WinnerTimeline wtl) {
		if(null != wtl) {
			if(null == wtl.get("id")) {
				wtl.save();
			} else {
				wtl.update();
			}
		}
	}
}
