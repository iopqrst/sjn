package com.sjn.service;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.sjn.model.LifeCategory;
import com.sjn.thread.ParamInit;
import com.sjn.utils.EhcacheFactoryUtils;

public class LifeCategoryService extends BaseService{

	/**
	 * 查询用户可以使用的类别
	 * 
	 * @param uid
	 * @return
	 */
	public List<Record> queryCategoryByUid(String uid) {
		return Db
				.find("select * from sjn_life_category where uid = ? or uid is null order by id asc",
						uid);
	}

	/**
	 * 查询用户最后一次添加的类别
	 * 
	 * @param uid
	 * @return
	 */
	public LifeCategory queryLastCategory(String uid) {
		return LifeCategory.dao
				.findFirst(
						"select * from sjn_life_category where uid = ? order by id desc limit 1",
						uid);
	}

	/**
	 * 根据uid 和 cid 查找类别 （理论上只用cid就能查询到相应的类别，这里又添加uid是为了判断这个类别真正属于这个人，而非盗用）
	 * 
	 * @param id
	 * @param uid
	 * @return
	 */
	public LifeCategory queryById(String id, String uid) {
		return LifeCategory.dao.findFirst(
				"select * from sjn_life_category where id = ? and uid = ?", id,
				uid);
	}

	/**
	 * 查询自己创建的类别，不包含系统共用的
	 * 
	 * @param uid
	 * @return
	 */
	public List<LifeCategory> querySelfCat(String uid) {
		return LifeCategory.dao.find(
				"select name,style from sjn_life_category where uid = ?", uid);
	}

	/**
	 * 根据uid和cat id删除cid （删除的时候要判断在sjn_life_item中是否使用了，如果使用了就不恩直接删除，否则直接删就行了）
	 * 
	 * @param id
	 *            cat id
	 * @param uid
	 *            用户id
	 * @return 返回删除的结果： 0删除成功 1类别被使用，需要修改项目类别以后才能删除类别，返回存在的数量
	 */
	@Before(Tx.class)
	public long delete(String id, String uid) {
		Record r = Db
				.findFirst(
						"select count(*) total from sjn_life_item where uid = ? and category = ?",
						uid, id);

		long total = r.getLong("total");
		if (0 == total) {
			Db.deleteById("sjn_life_category", "id", id);
			
			//将修改后的结果保存到缓存中
			EhcacheFactoryUtils cacheFactory = EhcacheFactoryUtils
					.getInstance();
			cacheFactory.delete(EhcacheFactoryUtils.cache_name_system,
					ParamInit.cacheStart_life_category + id);
			
			return 0;
		} else {
			return total;
		}
	}

	/**
	 * 替换删除cat
	 * @param targetCat 替换成的值
	 * @param delCat 要被删除的
	 */
	@Before(Tx.class)
	public int replaceAndDelCat(String uid, Integer targetCat, Integer delCat) {
		Db.update("update sjn_life_item set category = ? where uid = ? and category = ?", targetCat, uid, delCat);
		Db.deleteById("sjn_life_category", delCat);
		
		// 删除缓存中的类别
		EhcacheFactoryUtils cacheFactory = EhcacheFactoryUtils
				.getInstance();
		cacheFactory.delete(EhcacheFactoryUtils.cache_name_system,
				ParamInit.cacheStart_life_category + delCat);
		
		return 1;
	}

}
