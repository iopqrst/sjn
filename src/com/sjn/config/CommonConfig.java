package com.sjn.config;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.sjn.constant.Constant;
import com.sjn.controller.IndexController;
import com.sjn.controller.LifeCategoryController;
import com.sjn.controller.LifeItemController;
import com.sjn.controller.LoginController;
import com.sjn.controller.RegisterController;
import com.sjn.controller.WinnerController;
import com.sjn.handler.GlobalHandler;
import com.sjn.interceptor.AuthenticationInterceptor;
import com.sjn.model.LifeCategory;
import com.sjn.model.LifeItem;
import com.sjn.model.Timeline;
import com.sjn.model.Vcode;
import com.sjn.model.Winner;
import com.sjn.model.WinnerSettings;
import com.sjn.model.WinnerTimeline;
import com.sjn.thread.ParamInit;

/**
 * API引导式配置
 */
public class CommonConfig extends JFinalConfig {
	
	/**
	 * 保存系统配置参数值
	 */
	private static Map<String, Object> paramMap = new HashMap<String, Object>();
	
	/**
	 * 获取系统配置参数值
	 * @param key
	 * @return
	 */
	public static Object getParamMapValue(String key){
		return paramMap.get(key);
	}
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile("init.properties");
		
		//初始系统设置
		paramMap.put(Constant.config_securityKey_key, getProperty(Constant.config_securityKey_key).trim());
		paramMap.put(Constant.config_passErrorCount_key, getPropertyToInt(Constant.config_passErrorCount_key, 3));
		paramMap.put(Constant.config_passErrorHour_key, getPropertyToInt(Constant.config_passErrorHour_key, 3));
		paramMap.put(Constant.config_sessionMin_key, getPropertyToInt(Constant.config_sessionMin_key, 30));

		paramMap.put(Constant.js_version_key, getProperty(Constant.js_version_key).trim());
		
		me.setDevMode(getPropertyToBoolean("devMode", false));
		
		me.setViewType(ViewType.JSP);
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/", IndexController.class);
		me.add("/login", LoginController.class, "/WEB-INF/w/");
		me.add("/reg", RegisterController.class, "/WEB-INF/w/");
		me.add("/win", WinnerController.class, "/WEB-INF/w/");
		me.add("/life", LifeItemController.class, "/WEB-INF/w/");
		me.add("/cat", LifeCategoryController.class, "/WEB-INF/w/");
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
//		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
//		me.add(c3p0Plugin);
		
		// druid 数据库连接插件
		DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		me.add(druidPlugin);
		
		// 配置缓存
		me.add(new EhCachePlugin()); // EhCache缓存
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		me.add(arp);
		arp.addMapping("sjn_winner", "uid", Winner.class);
		arp.addMapping("sjn_life_category", "id", LifeCategory.class);
		arp.addMapping("sjn_life_item", "lid", LifeItem.class);
		arp.addMapping("sjn_winner_settings", "uid", WinnerSettings.class);
		arp.addMapping("sjn_timeline", "id", Timeline.class);
		arp.addMapping("sjn_winner_timeline", "id", WinnerTimeline.class);
		arp.addMapping("sjn_vcode", "vid", Vcode.class);
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		
//		me.add(new TxByActionKeys(""));
//		me.add(new TxByActionMethods(""));
//		me.add(new TxByRegex(""));
//		me.add(new TxByRegex("", true));
		
		me.add(new SessionInViewInterceptor()); // 支持在使用session
		me.add(new AuthenticationInterceptor());
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		me.add(new GlobalHandler());
	}
	
	/**
	 * 系统启动完成后执行
	 */
	public void afterJFinalStart() {
		new ParamInit().start(); // 缓存参数
	}
	
	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 1988, "/", 5);
	}
}
