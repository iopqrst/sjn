package com.sjn.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.sjn.config.CommonConfig;
import com.sjn.constant.Constant;
import com.sjn.model.Winner;
import com.sjn.thread.ParamInit;
import com.sjn.utils.ContextUtils;
import com.sjn.utils.DateUtils;
import com.sjn.utils.EhcacheFactoryUtils;
import com.sjn.utils.StringUtils;
import com.sjn.utils.ToolSecurityPbkdf2;

public class WinnerService extends BaseService {

	@Before(Tx.class)
	public Winner save(Winner winner) {

		// TODO 手机号唯一校验和密码的有效性没有做校验

		try {
			// 密码加密
			byte[] salt = ToolSecurityPbkdf2.generateSalt();// 密码盐
			byte[] encryptedPassword = ToolSecurityPbkdf2.getEncryptedPassword(
					winner.getStr("password"), salt);

			winner.set("salt", salt);
			winner.set("password", encryptedPassword);
			winner.set("uid", StringUtils.getUuidByJdk(true));
			winner.set("createTime", new Date());
			winner.set("status", Winner.STATUS_NORMAL);

			// 保存用户信息
			winner.save();

			// 将用户信息保存到缓存中
			saveToCache(winner);

			// 初始其他参数信息
			initTimeline(winner);

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("保存用户密码加密操作异常");
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException("保存用户密码加密操作异常");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("保存用户异常");
		}
		return winner;
	}

	public int validateWinner(HttpServletRequest request,
			HttpServletResponse response, String mobile, String password,
			boolean autoLogin) {
		Winner winner = null;
		// 1. 获取用户信息
		Object obj = EhcacheFactoryUtils.getInstance().get(
				EhcacheFactoryUtils.cache_name_system,
				ParamInit.cacheStart_winner + mobile);

		if (null != obj) {
			winner = (Winner) obj;
		} else {
			Winner win = queryWinnerByMobile(mobile);

			if (null != win) { // 存在该用户
				winner = win;
			} else { // 不存在该用户
				return Constant.login_info_not_exist;
			}
		}

		// 2. 判断用户的状态
		int status = winner.getInt("status");
		if (Winner.STATUS_FORBIDDEN == status) {
			return Constant.login_info_forbidden; // 账号被冻结
		}

		// TODO 如果要启用该部分功能需要开启一个定时器去定时执行还原用户状态
		// 3. 密码错误次数超限
		int errorCount = winner.getInt("errorcount");
		int passErrorCount = (Integer) CommonConfig
				.getParamMapValue(Constant.config_passErrorCount_key);
		
		if (errorCount >= passErrorCount) {
			Date stopDate = winner.getDate("forbidTime");
			int hourSpace = DateUtils.getDateHourSpace(stopDate, new Date());
			int passErrorHour = (Integer) CommonConfig
					.getParamMapValue(Constant.config_passErrorHour_key);
			if (hourSpace < passErrorHour) {
				return Constant.login_info_miss_count;// 密码错误次数超限，几小时内不能登录
			} else {
				Db
						.update(
								" update sjn_winner w set w.forbidTime=null, w.errorcount=0 where w.uid = ? ",
								winner.getStr("uid"));
				
				updateWinnerCache(winner.getStr("mobile"));
			}
		}

		// 4.验证密码
		byte[] salt = winner.getBytes("salt");// 密码盐
		byte[] encryptedPassword = winner.getBytes("password");
		boolean bool = false;
		try {
			bool = ToolSecurityPbkdf2.authenticate(password, encryptedPassword,
					salt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		if (bool) {
			// 密码验证成功
			ContextUtils.setCurrentUser(request, response, winner, autoLogin);// 设置登录账户
			return Constant.login_info_success;
		} else {
			// 密码验证失败
			Db
					.update(
							" update sjn_winner w set w.forbidTime = ? ,w.errorcount = ? where w.uid = ? ",
							new Date(), errorCount + 1, winner.get("uid"));
			
			updateWinnerCache(winner.getStr("mobile"));
			
			return Constant.login_info_pwd_wrong;
		}
	}

	/**
	 * 获取手机号码的数量
	 * @param mobile
	 */
	public long getMobileCount(String mobile) {
		String sql = "select count(*) total from sjn_winner where mobile = ?";
		Record r = Db.findFirst(sql, mobile);
		return r.getLong("total");
	}
	
	/**
	 * 保存到缓存中
	 * @param winner
	 */
	private void saveToCache(Winner winner) {
		// 缓存
		EhcacheFactoryUtils cacheFactory = EhcacheFactoryUtils.getInstance();

		cacheFactory.add(EhcacheFactoryUtils.cache_name_system,
				ParamInit.cacheStart_winner + winner.getStr("uid"), winner);
		cacheFactory.add(EhcacheFactoryUtils.cache_name_system,
				ParamInit.cacheStart_winner + winner.getStr("mobile"), winner);
	}

	/**
	 * 为用户生成默认的时间轴时间
	 * 
	 * @param winner
	 */
	private void initTimeline(Winner winner) {
		Record r = new Record();
		r.set("uid", winner.getStr("uid"));
		r.set("beginPolt", "6.5");
		r.set("endPolt", "23.5");

		TimelineService timelineService = new TimelineService();
		timelineService.generateTimeline(r);
	}
	
	/**
	 * 根据手机号查询用户信息
	 * @param mobile
	 * @return
	 */
	private Winner queryWinnerByMobile(String mobile) {
		String sql = "select * from sjn_winner where mobile = ? and status = 0 order by createTime asc limit 1";

		Winner win = Winner.dao.findFirst(sql, mobile);
		return win;
	}
	
	/**
	 * 更新用户缓存
	 * @param mobile
	 */
	private void updateWinnerCache(String mobile) {
		
		Winner winner = queryWinnerByMobile(mobile);
		
		if(null != winner) {
			// 缓存
			EhcacheFactoryUtils cacheFactory = EhcacheFactoryUtils.getInstance();

			cacheFactory.update(EhcacheFactoryUtils.cache_name_system,
					ParamInit.cacheStart_winner + winner.getStr("uid"), winner);
			cacheFactory.update(EhcacheFactoryUtils.cache_name_system,
					ParamInit.cacheStart_winner + winner.getStr("mobile"), winner);
		}
	}

}
