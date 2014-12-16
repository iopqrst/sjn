package com.sjn.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class TimelineService extends BaseService {

	public List<Record> queryTimeline(String uid) {
		Record r = Db
				.findFirst(
						"select beginPolt, endPolt from sjn_winner_timeline where uid = ? ",
						uid);
		if (null != r) {
			return Db
					.find(
							"select * from sjn_timeline where val >= ? and val <= ? order by id asc",
							new Object[] {
									r.getDouble("beginPolt"),
									r.getDouble("endPolt") < 0.5 ? 23.5 : r
											.getDouble("endPolt") });
			// 0.5其实就是为了判断0的情况，因为结束时间可以为0，此时表示的意义为24点
		} else {
			return null;
		}
	}

	public void generateTimeline(Record record) {
		Db.save("sjn_winner_timeline", record);
	}
}
