package com.sjn.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjn.model.LifeItem;
import com.sjn.model.Timeline;
import com.sjn.utils.DateUtils;
import com.sjn.utils.StringUtils;
import com.sjn.utils.query.PageObject;
import com.sjn.utils.query.QueryCondition;

@SuppressWarnings("unchecked")
public class LifeItemService extends BaseService {

	/**
	 * 查询用户最近一天的life item (默认查询当天的，如果当天没有则查询最近一次的数据)
	 * 
	 * @param date
	 *            当天或者最近一天时间
	 * @param uid
	 *            用户id
	 * @return life items
	 */
	public List<LifeItem> queryLastestDayList(Date date, String uid) {

		List<LifeItem> list = LifeItem.dao
				.find("select * from sjn_life_item where whichDay = ? and uid = ? order by beginPolt asc",
						new Object[] { date, uid });

		if (list.isEmpty()) {
			Record record = Db
					.findFirst(
							"select whichDay from sjn_life_item where uid = ? order by whichDay desc LIMIT 1",
							uid);

			if (null != record) {
				Date whichDay = record.getDate("whichDay");

				if (null == whichDay) {
					return null;
				} else {
					this.queryLastestDayList(whichDay, uid);
				}
			}

		}

		return list;
	}

	public void update(LifeItem li) {

		String lid = li.getStr("lid");
		LifeItem lifeItem = LifeItem.dao.findById(lid);

		lifeItem.set("beginPolt", li.getDouble("beginPolt"));
		lifeItem.set("endPolt", li.getDouble("endPolt"));
		lifeItem.set("interval", li.getDouble("interval"));
		lifeItem.set("category", li.getInt("category"));
		lifeItem.set("doWhat", li.getStr("doWhat"));

		lifeItem.update();

	}

	/**
	 * 查看是否存在相同时间节点的item（因为添加和修改公用一个方法，所有需要判断操作类型，修改需要排除自身在外）
	 * 
	 * @param li
	 *            item 类型
	 * @param opearte
	 *            (modify or add)
	 * @return 已经存在时间节点冲突节点集合
	 */
	public List<Record> queryExistItems(LifeItem li, String opearte) {

		String sql = "SELECT * FROM sjn_life_item WHERE uid = ? "
				+ " AND whichDay = ?"
				+ " AND ((beginPolt >= ? AND beginPolt < ? ) OR (endPolt > ? AND endPolt <= ?))";

		List<Object> margs = new ArrayList<Object>();
		margs.add(li.getStr("uid"));
		margs.add(DateUtils.cnParse(DateUtils.cnFormat(li.getDate("whichDay"))));
		margs.add(li.getDouble("beginPolt"));
		margs.add(li.getDouble("endPolt"));
		margs.add(li.getDouble("beginPolt"));
		margs.add(li.getDouble("endPolt"));

		if ("modify".equals(opearte)) {
			sql += " and lid != ?";
			margs.add(li.get("lid"));
		}

		List<Record> list = Db.find(sql, margs.toArray());

		return list;
	}

	public void delete(String lid, String loginUid) {
		Db.delete("sjn_life_item", "lid", new Record().set("uid", loginUid)
				.set("lid", lid));
	}

	/**
	 * 查询用户默认开始时间
	 * 
	 * @param uid
	 */
	public Record queryDefaultTimeline(String uid) {

		String current = DateUtils.cnFormat(new Date());

		Record r = Db
				.findFirst(
						"select count(*) total from sjn_life_item where uid = ? and whichDay = ?",
						new Object[] { uid, current });

		Record returnVal = null;
		if (r.getLong("total") >= 0) {
			returnVal = Db
					.findFirst(
							"select endPolt as begin from sjn_life_item where uid = ? and whichDay = ? order by endPolt desc limit 1",
							new Object[] { uid, current });
		} else {
			returnVal = Db
					.findFirst(
							"select beginPolt as begin from sjn_winner_timeline where uid = ?",
							uid);
		}

		// 将时间节点对应值也保存到record中，方便页面取值
		if (null != returnVal) {
			returnVal.set("timeline",
					Timeline.getTimePolt(returnVal.getDouble("begin")));
		}

		return returnVal;
	}

	/**
	 * 查询上一次相同时间节点的类别和做的事情
	 * 
	 * @param li
	 *            参数对象
	 * @return 返回 节点类别和doWhat
	 */
	public Record queryLastData(LifeItem li) {

		Record r = Db
				.findFirst(
						"select category,doWhat from sjn_life_item where uid = ? and beginPolt = ? and endPolt = ? order by whichDay desc limit 1",
						new Object[] { li.getStr("uid"),
								li.getDouble("beginPolt"),
								li.getDouble("endPolt") });
		return r;
	}

	/**
	 * 分页查询life item
	 * 
	 * @param model
	 *            查询条件
	 */
	public Page<?> queryObject(LifeItem model, PageObject pageObject, QueryCondition qc) {
		StringBuffer sql = new StringBuffer(
				"select * from sjn_life_item where 1 = 1");

		ArrayList args = new ArrayList();

		if (null != model) {
			if (!StringUtils.isEmpty(model.getStr("uid"))) {
				sql.append(" and uid = ?");
				args.add(model.getStr("uid"));
			}

			if (null != model.getInt("category")) {
				sql.append(" and category = ?");
				args.add(model.getInt("category"));
			}

			if (null != model.getDate("whichDay")) {
				sql.append(" and whichDay = ?");
				args.add(model.getDate("whichDay"));
			}
		}
		
		if( null != qc) {
			if( null != qc.getBeginTime()) {
				sql.append(" and whichDay >= ?");
				args.add(qc.getBeginTime());
			}
			
			if( null != qc.getEndTime()) {
				sql.append(" and whichDay <= ?");
				args.add(qc.getEndTime());
			}
		}
		
		sql.append(" order by whichDay, beginPolt");

		return this.queryObject(pageObject, sql, args.toArray());
	}

	/**
	 * 根据category 查询life items
	 * 
	 * @param uid
	 * @param cat
	 */
	public Page<?> queryItemByCat(String uid, int cat, PageObject pageObject) {
		LifeItem li = new LifeItem();
		li.set("uid", uid);
		li.set("category", cat);
		return this.queryObject(li, pageObject, null);
	}
}
