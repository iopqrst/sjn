package com.sjn.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.sjn.model.LifeCategory;
import com.sjn.service.LifeCategoryService;
import com.sjn.service.LifeItemService;
import com.sjn.thread.ParamInit;
import com.sjn.utils.EhcacheFactoryUtils;
import com.sjn.utils.StringUtils;

public class LifeCategoryController extends BaseController {

	LifeCategoryService categoryService = new LifeCategoryService();
	LifeItemService itemService = new LifeItemService();
	
	public void index() {
		List<Record> clist = categoryService.queryCategoryByUid(getLoginUid());
		setAttr("catList", clist);
		setAttr("catSize", clist.size());

		render("category_list.jsp");
	}

	public void add() {
		render("category_add.jsp");
	}

	@Before({ POST.class })
	public void save() {
		String name = getPara("name");

		if (!StringUtils.isEmpty(name)) {
			LifeCategory lc = new LifeCategory();
			lc.set("uid", getLoginUid());
			lc.set("name", name);
			lc.set("style", getStyle());

			lc.save();

			//将最后一次（新）数据加入到缓存中
			LifeCategory llc = categoryService.queryLastCategory(getLoginUid());
			EhcacheFactoryUtils cacheFactory = EhcacheFactoryUtils
					.getInstance();
			cacheFactory.add(EhcacheFactoryUtils.cache_name_system,
					ParamInit.cacheStart_life_category + llc.getInt("id"),
					llc);

			JSONObject jo = new JSONObject();
			jo.put("id", this.encrypt(llc.getInt("id")+""));
			jo.put("status", "succ");
			jo.put("name", llc.getStr("name"));
			jo.put("style", llc.getStr("style"));
			renderJson(jo.toString());
			
		} else {
			renderJson("{\"status\" : \"params\"}");
		}

	}

	public void modify() {
		String id = this.decrypt(getPara(0));

		JSONObject jo = new JSONObject();
		if (!StringUtils.isEmpty(id)) {
			LifeCategory lc = categoryService.queryById(id, getLoginUid());
			
			jo.put("status", "succ");
			jo.put("cid", this.encrypt(lc.getInt("id") + ""));
			jo.put("name", null == lc.getStr("name") ? "" : lc.getStr("name"));
			
			String style = lc.getStr("style");
			if(!StringUtils.isEmpty(style)) {
				String[] styles = style.split(";");
				
				for(String css : styles) {
					if(css.indexOf("background:") >= 0) {
						jo.put("bgColor", css.substring(11));
					}
					if(css.indexOf("color:") >= 0) {
						jo.put("fontColor", css.substring(6));
					}
				}
			}
			
		} else {
			jo.put("status", "parmas");
		}
		renderJson(jo.toString());
	}

	@Before({ POST.class })
	public void update() {
		String id = this.decrypt(getPara(0));
		
		if (!StringUtils.isEmpty(id)) {
			String name = getPara("name");
			if(!StringUtils.isEmpty(name)) {
				LifeCategory ocat = categoryService.queryById(id, getLoginUid());
				ocat.set("style", getStyle());
				ocat.set("name", name);
				
				ocat.update();
				
				//将修改后的结果保存到缓存中
				EhcacheFactoryUtils cacheFactory = EhcacheFactoryUtils
						.getInstance();
				cacheFactory.add(EhcacheFactoryUtils.cache_name_system,
						ParamInit.cacheStart_life_category + ocat.getInt("id"),
						ocat);
			}
			renderJson("{\"status\" : \"succ\"}");
		} else {
			renderJson("{\"status\" : \"params\"}");
		}
	}
	
	/**
	 * 查询用户自己定义的类别（不包含公共的）
	 */
	public void querySelfCat() {
		List<LifeCategory> list = categoryService.querySelfCat(getLoginUid());
		if(null != list) {
			JSONObject jo = new JSONObject();
			System.out.println(list.size());
			jo.put("len", list.size());
			jo.put("data", JsonKit.toJson(list));
			renderJson(jo);
		} else {
			renderJson("{\"len\":\"0\"}");
		}
	}
	
	@Before({POST.class})
	public void del() {
		String id = decrypt(getPara(0));
		
		long result = categoryService.delete(id, getLoginUid());
		
		JSONObject jo = new JSONObject();
		if(0 == result) { //删除成功
			jo.put("status", "succ");
		} else {
			//存在已经使用的项目，返回使用给类别的项目，让其修改，如果太多的话值查询一部分，要不然也太多了
			jo.put("count", result);
			jo.put("status", "used");
		}
		renderJson(jo);
	}
	
	/**
	 * 删除替换类别
	 */
	@Before(POST.class)
	public void replaceAndDelCat() {
		String targetCat = this.decrypt(getPara(0)); //替换成的值
		String delCat = this.decrypt(getPara(1)); //要被删的值
		
		JSONObject jo = new JSONObject();
		if(null == targetCat || null == delCat) {
			jo.put("s", "0"); //失败
		} else {
			try {
				int result = categoryService.replaceAndDelCat(
						getLoginUid(),
						Integer.parseInt(targetCat), 
						Integer.parseInt(delCat));

				if(result > 0) {
					jo.put("s", "1"); //失败
				} else {
					jo.put("s", "0"); //失败
				}
				
			} catch (NumberFormatException e) {
				jo.put("s", "0"); //失败
				e.printStackTrace();
			}
		}
		renderJson(jo.toString());
	}
	
	private String getStyle() {
		String bgColor = getPara("bgColor");
		String fontColor = getPara("fontColor");
		
		String style = "";

		if (!StringUtils.isEmpty(bgColor)) {
			style += "background:" + bgColor + ";";
		}

		if (!StringUtils.isEmpty(fontColor)) {
			style += "color:" + fontColor + ";";
		}
		return style;
	}

}
