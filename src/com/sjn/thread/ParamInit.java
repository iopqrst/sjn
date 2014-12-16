package com.sjn.thread;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.sjn.model.LifeCategory;
import com.sjn.model.Timeline;
import com.sjn.model.Winner;
import com.sjn.utils.EhcacheFactoryUtils;

/**
 * 系统初始化缓存操作类
 * 
 * @author Administrator
 */
public class ParamInit extends Thread {

	private static Logger log = Logger.getLogger(ParamInit.class);

	public static String cacheStart_timeline = "timeline_";
	public static String cacheStart_winner = "winner_";
	public static String cacheStart_life_category = "life_category_";

	@Override
	public void run() {
		log.info("缓存参数初始化 start ...");

		// 1.缓存时间轴
		cacheTimeline();
		// 2.用户信息
		cacheWinner();
		// 3.用户设置信息
		cacheWinnerSettings();
		// 4.life category
		cacheLifeCategory();

		log.info("缓存参数初始化 end ...");
	}

	/**
	 * 缓存时间轴常量信息
	 */
	public static void cacheTimeline() {
		EhcacheFactoryUtils cacheFactory = EhcacheFactoryUtils.getInstance();
		List<Timeline> timelineList = Timeline.dao
				.find("select * from sjn_timeline order by id asc");

		for (Timeline timeline : timelineList) {
			cacheFactory.add(EhcacheFactoryUtils.cache_name_system,
					ParamInit.cacheStart_timeline + timeline.getDouble("val"),
					timeline);
			timeline = null;
		}

		timelineList = null;
	}

	/**
	 * 缓存winner信息
	 */
	public static void cacheWinner() {
		EhcacheFactoryUtils cacheFactory = EhcacheFactoryUtils.getInstance();
		List<Winner> winnerList = Winner.dao
				.find("select * from sjn_winner order by uid");

		for (Winner winner : winnerList) {
			cacheFactory.add(EhcacheFactoryUtils.cache_name_system,
					ParamInit.cacheStart_winner + winner.getStr("uid"), winner);
			cacheFactory.add(EhcacheFactoryUtils.cache_name_system,
					ParamInit.cacheStart_winner + winner.getStr("mobile"),
					winner);
			winnerList = null;
		}
		winnerList = null;
	}

	/**
	 * 缓存winner信息
	 */
	public static void cacheWinnerSettings() {
		// EhcacheFactoryUtils cacheFactory = EhcacheFactoryUtils.getInstance();
		// List<Timeline> timelineList = Timeline.dao
		// .find("select * from sjn_timeline order by id asc");
		//
		// for (Timeline timeline : timelineList) {
		// cacheFactory.add(EhcacheFactoryUtils.cache_name_system,
		// ParamInit.cacheStart_timeline + timeline.getDouble("val"),
		// timeline);
		// timeline = null;
		// }
		//
		// timelineList = null;
	}

	/**
	 * 缓存life item's category
	 */
	public static void cacheLifeCategory() {
		EhcacheFactoryUtils cacheFactory = EhcacheFactoryUtils.getInstance();
		List<LifeCategory> categoryList = LifeCategory.dao
				.find("select * from sjn_life_category order by id asc");

		for (LifeCategory category : categoryList) {
			if (!StringUtils.isEmpty(category.getStr("uid"))) { // 用户设置的类别
				cacheFactory.add(
						EhcacheFactoryUtils.cache_name_system,
						ParamInit.cacheStart_life_category
								+ category.getStr("uid"), category);
			} // 公用的常量
			cacheFactory.add(EhcacheFactoryUtils.cache_name_system,
					ParamInit.cacheStart_life_category + category.getInt("id"),
					category);

			category = null;
		}

		categoryList = null;
	}

}
