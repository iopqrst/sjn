package com.sjn.controller;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjn.model.LifeItem;
import com.sjn.model.Timeline;
import com.sjn.service.LifeCategoryService;
import com.sjn.service.LifeItemService;
import com.sjn.service.TimelineService;
import com.sjn.utils.DateUtils;
import com.sjn.utils.StringUtils;
import com.sjn.utils.query.PageObject;
import com.sjn.utils.query.QueryCondition;
import com.sjn.validator.LifeItemValidator;

public class LifeItemController extends BaseController {

	LifeItemService itemService = new LifeItemService();
	LifeCategoryService categoryService = new LifeCategoryService();
	TimelineService timelineService = new TimelineService();

	public void index() {
		String strCurrent = DateUtils.cnFormat(new Date());
		Date current = DateUtils.cnParse(strCurrent);
		List<LifeItem> itemList = itemService.queryLastestDayList(current,
				getLoginUid());
		setAttr("itemList", itemList);
		setAttr("itemSize", itemList.size());
		setAttr("current", "今日（"+ strCurrent +"）");

		getLifeCategory();
		render("life_list.jsp");
	}
	
	/**
	 * 根据条件查询life item
	 */
	public void history() {
		Date begin = DateUtils.cnParse(getPara("search_begin"));
		Date end = DateUtils.cnParse(getPara("search_end"));
		
		LifeItem itemParams = this.getModel(LifeItem.class);
		QueryCondition qc = null;
		
		if(null != begin || null != end) {
			qc = new QueryCondition();
			
			if(null != begin) {
				qc.setBeginTime(begin);
				setAttr("search_begin", getPara("search_begin"));
			}
			
			if(null != end) {
				qc.setEndTime(end);
				setAttr("search_end", getPara("search_end"));
			}
		} else {
			String strCurrent = DateUtils.cnFormat(new Date());
			Date current = DateUtils.cnParse(strCurrent);
			itemParams.set("whichDay", current);
			setAttr("current", "今日（"+ strCurrent +"）");
		}
		
		
		PageObject pageObject = getPageObject();
		pageObject.setPageSize(5000);
		Page<?> pageItems = itemService.queryObject(itemParams, pageObject, qc);
		
		setAttr("itemList", pageItems.getList());
		setAttr("itemSize", pageItems.getTotalRow());
		
		getLifeCategory();
		render("life_list.jsp");
	}

	/**
	 * 寻找、探索 (to add page)
	 */
	public void seek() {
		getLifeCategory();
		getWinnerTimeline();

		getDefaultTimeline(getLoginUid());

		setAttr("dateList", DateUtils.getLastestDays(7));

		render("life_add.jsp");
	}

	/**
	 * time flies （create life items）
	 */
	@Before( { LifeItemValidator.class, POST.class })
	public void timeFlies() {

		String render = "/life/seek?e=x";

		LifeItem pageItem = this.getModel(LifeItem.class);
		if (!validParams(render, pageItem)) {
			return;
		}

		//处理换行符
		pageItem.set("doWhat", handleNewlineCharacter(pageItem.getStr("doWhat")));
		
		pageItem.set("uploadTime", new Date());
		pageItem.set("uid", getLoginUid()); // 要从session中获取用户的id

		pageItem.save();

		redirect("/life");
	}

	/**
	 * regret (to modify page)
	 */
	public void regret() {
		String lid = getPara(0); // 获取加密后的lid

		lid = this.decrypt(lid);

		if (!StringUtils.isEmpty(lid)) {
			LifeItem litem = LifeItem.dao.findById(lid);
			if (null != litem && litem.getStr("uid").equals(getLoginUid())) {
				setAttr("item", litem);
			} else {
				setAttr("msg", "没有查到对应信息");
			}

			getLifeCategory();
			getWinnerTimeline();
			
			setAttr("dateList", DateUtils.getLastestDays(7));
		} else {
			setAttr("msg", "好像缺了点什么，没找到对应信息");
		}

		render("life_modify.jsp");
	}

	@Before( { POST.class })
	public void modify() {
		LifeItem pageItem = this.getModel(LifeItem.class);
		String lid = getPara(0);

		lid = this.decrypt(lid);

		String render = "/life/regret/" + getPara(0) + "?e=x";
		if (!StringUtils.isEmpty(lid)) {

			if (!validParams(render, pageItem)) {
				return;
			}
			
			//处理换行符
			pageItem.set("doWhat", handleNewlineCharacter(pageItem.getStr("doWhat")));

			pageItem.set("lid", lid);
			itemService.update(pageItem);
			redirect("/life");
		} else {
			setAttr("msg", "好像缺了点什么，没找到对应信息");
			handleError(render);
		}
	}

	/**
	 * 判断当前时间段是否已经存在，如果存在当然不能添加了
	 */
	public void existItems() {

		String lid = this.decrypt(getPara("lid"));

		LifeItem item = this.getModel(LifeItem.class);
		item.set("uid", getLoginUid());
		if (!StringUtils.isEmpty(lid)) {
			item.set("lid", lid);
		}

		List<Record> list = itemService.queryExistItems(item,
				getPara("operate"));

		JSONObject jo = new JSONObject();
		jo.put("size", list.size());

		if (list.size() > 0) {
			JSONArray ja = new JSONArray();

			for (Record record : list) {
				JSONObject jj = new JSONObject();
				jj.put("begin", Timeline.getTimePolt(record
						.getDouble("beginPolt")));
				jj
						.put("end", Timeline.getTimePolt(record
								.getDouble("endPolt")));
				jj.put("interval", record.getDouble("interval"));
				ja.add(jj);
			}

			jo.put("detail", ja);
		}
		renderJson(jo.toString());
	}

	/**
	 * 删除
	 */
	public void del() {
		String lid = getPara(0);
		lid = this.decrypt(lid);

		itemService.delete(lid, getLoginUid());
		redirect("/life");
	}

	/**
	 * 查询上次时间（必须是开始时间和结束时间节点是相同的，并且是最近一次的）
	 */
	public void queryLastData() {
		Record r = itemService.queryLastData(this.getModel(LifeItem.class).set(
				"uid", getLoginUid()));
		if (null != r) {
			renderJson("{\"category\":" + r.getInt("category")
					+ ",\"doWhat\":\"" + r.get("doWhat", "") + "\"}");
		} else {
			renderJson("{}");
		}
	}

	/**
	 * 分页查询
	 */
	@SuppressWarnings("unchecked")
	public void queryByCat() {
		
		String cid = this.decrypt(getPara("category"));
		
		int catId = 0;
		try {
			catId = Integer.parseInt(cid);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		Page<?> page = itemService.queryItemByCat(getLoginUid(), catId, getPageObject());
		List<Record> list = (List<Record>)page.getList();
		
		JSONObject jresult = new JSONObject();
		
		if(null == list || list.size() == 0) {
			jresult.put("total", 0);
		} else {
			jresult.put("total", page.getTotalRow());
			
			JSONArray ja = new JSONArray();
			
			for (Record record : list) {
				JSONObject joRecord = new JSONObject();
				joRecord.put("begin", Timeline.getTimePolt(record.getDouble("beginPolt")));
				joRecord.put("end", Timeline.getTimePolt(record.getDouble("endPolt")));
				joRecord.put("interval", record.getDouble("interval"));
				joRecord.put("whichDay", DateUtils.cnFormat(record.getDate("whichDay")));
				
				ja.add(joRecord);
			}
			
			jresult.put("data", ja);
			
		}
		//System.out.println(jresult.toString());
		renderJson(jresult.toString());
	}
	
	/**
	 * 替换换行符，以免出现换行符导致ajax调用出现异常
	 * @return
	 */
	private String handleNewlineCharacter(String content) {
		if(!StringUtils.isEmpty(content)) {
			return content.replace("\r\n", "").replace("\n", "");
		}
		return content;
	}

	/**
	 * 获取用户的所有life category
	 */
	private void getLifeCategory() {
		List<Record> categoryList = categoryService
				.queryCategoryByUid(getLoginUid());
		setAttr("categoryList", categoryList);
	}

	private void getWinnerTimeline() {
		List<Record> timeLineList = timelineService
				.queryTimeline(getLoginUid());
		setAttr("timeLineList", timeLineList);
	}

	/**
	 * 验证输入条件
	 * @param render
	 * @param pageItem
	 * @return
	 */
	private boolean validParams(String render, LifeItem pageItem) {

		if (null == pageItem) {
			handleError(render);
			return false;
		}

		try {
			// 不允许给未来添加事件
			if (null != pageItem.getDate("whichDay")) {

				if ((pageItem.getDate("whichDay")).after(
						new Date())) {
					System.out.println("-----------------填写时间不符合");
					handleError(render);
					return false;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			handleError(render);
			return false;
		}

		try {
			double beginPolt = pageItem.getDouble("beginPolt");
			double endPolt = pageItem.getDouble("endPolt");

			if (endPolt == 0) {
				endPolt = 24; // 因为结束节点时间最晚可以是24点（此时因为设置为0,需要纠正一下时间）
			}

			double interval = endPolt - beginPolt; // 做某件事情花费的时间

			if (interval <= 0 || interval % 0.5 != 0) {
				System.out.println("选择时间出现问题，结束时间与开始时间差不能小于等于0，或者无法整除0.5");
				handleError(render);
				return false;
			}

			pageItem.set("interval", interval);
		} catch (Exception e) {
			e.printStackTrace();
			handleError(render);
			return false;
		}

		String cid = this.decrypt(getPara("category"));
		if (!StringUtils.isEmpty(cid)) {
			pageItem.set("category", Integer.parseInt(cid));
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 处理异常信息
	 * 
	 * @param render
	 * @param error
	 */
	private void handleError(String render) {
		redirect(render, true);
	}

	/**
	 * 获取用户开始时间值得默认值 （如果是当天第一条则查询用户设置的开始时间，否则查询用户最后一条结束时间（非设置的结束时间）
	 * 
	 * @param loginUid
	 */
	private void getDefaultTimeline(String uid) {
		Record r = itemService.queryDefaultTimeline(uid);
		setAttr("sdefault", r);
	}
}
