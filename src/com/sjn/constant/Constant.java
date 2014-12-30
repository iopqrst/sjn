package com.sjn.constant;

public class Constant {
	
	public static final String WINNER_SESSION = "session_of_winner";

	/**
	 * 加密
	 */
	public static final String config_securityKey_key = "config.securityKey";
	
	/**
	 * 密码最大错误次数
	 */
	public static final String config_passErrorCount_key = "config.passErrorCount";
	
	/**
	 * 密码错误最大次数后间隔登陆时间（小时）
	 */
	public static final String config_passErrorHour_key = "config.passErrorHour";
	
	/**
	 * session 时间 （分钟）
	 */
	public static final String config_sessionMin_key = "config.sessionMin";
	
	/**
	 * js 版本信息
	 */
	public static final String js_version_key = "js.version";
	
	/**
	 * 用户登录状态码
	 */
	public static final int login_info_not_exist = 0;// 用户不存在
	public static final int login_info_forbidden = 1;// 停用账户
	public static final int login_info_miss_count = 2;// 密码错误次数超限
	public static final int login_info_success = 3;// 密码验证成功
	public static final int login_info_pwd_wrong = 4;// 密码验证失败
	
	public static final int INTERFACE_PARAMAS_ERROR = -1 ; // 参数不正确
	public static final int INTERFACE_FAIL = 0 ; // 接口请求失败
	public static final int INTERFACE_SUCCESS = 1; //接口请求成功

}
