package com.sjn.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

	private static final String cnPattern = "yyyy-MM-dd";
	private static final String longPattern = "yyyy-MM-dd HH:mm:ss";

	public static String cnFormat(Object date) {
		return format(date, cnPattern);
	}

	public static String longFormat(Object date) {
		return format(date, longPattern);
	}

	public static String format(Object date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static Date cnParse(String date) {
		return parse(date, cnPattern);
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static Date longParse(String date) {
		return parse(date, longPattern);
	}

	/**
	 * 解析
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String date, String pattern) {
		if(null == date || "".equals(date)) return null;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 两个日期的时间差，返回"X天X小时X分X秒"
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String getBetween(Date begin, Date end) {
		long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
		long day = between / (24 * 3600);
		long hour = between % (24 * 3600) / 3600;
		long minute = between % 3600 / 60;
		long second = between % 60 / 60;

		StringBuffer sb = new StringBuffer();
		sb.append(day);
		sb.append("天");
		sb.append(hour);
		sb.append("小时");
		sb.append(minute);
		sb.append("分");
		sb.append(second);
		sb.append("秒");

		return sb.toString();
	}

	/**
	 * 返回两个日期之间隔了多少小时
	 * 
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return
	 */
	public static int getMinutesSpace(Date start, Date end) {
		int mins = (int)((end.getTime() - start.getTime()) / (1000 * 60));
		return mins;
	}
	
	/**
	 * 返回两个日期之间隔了多少小时
	 * 
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return
	 */
	public static int getDateHourSpace(Date start, Date end) {
		int hour = (int) ((end.getTime() - start.getTime()) / 3600 / 1000);
		return hour;
	}

	/**
	 * 返回两个日期之间隔了多少天
	 * 
	 * @param 开始时间
	 * @param 结束时间
	 * @return
	 */
	public static int getDateDaySpace(Date start, Date end) {
		int day = (int) (getDateHourSpace(start, end) / 24);
		return day;
	}

	/**
	 * 得到某一天是星期几
	 * 
	 * @param strDate
	 *            日期字符串
	 * @return int 星期几（-1异常）
	 */
	@SuppressWarnings("static-access")
	public static int getDateInWeek(String date) {
		DateFormat format = DateFormat.getDateInstance();
		try {
			format.parse(date);
			java.util.Calendar c = format.getCalendar();
			int day = c.get(c.DAY_OF_WEEK) - c.SUNDAY;
			return day;
		} catch (ParseException e) {
			return -1;
		}
	}

	/**
	 * 日期减去多少个小时
	 * 
	 * @param date
	 * @param hourCount
	 *            多少个小时
	 * @return
	 */
	public static Date getDateReduceHour(Date date, long hourCount) {
		long time = date.getTime() - 3600 * 1000 * hourCount;
		Date dateTemp = new Date();
		dateTemp.setTime(time);
		return dateTemp;
	}

	/**
	 * 日期区间分割
	 * 
	 * @param start
	 * @param end
	 * @param splitCount
	 * @return
	 */
	public static List<Date> getDateSplit(Date start, Date end, long splitCount) {
		long startTime = start.getTime();
		long endTime = end.getTime();
		long between = endTime - startTime;

		long count = splitCount - 1l;
		long section = between / count;

		List<Date> list = new ArrayList<Date>();
		list.add(start);

		for (long i = 1l; i < count; i++) {
			long time = startTime + section * i;
			Date date = new Date();
			date.setTime(time);
			list.add(date);
		}

		list.add(end);

		return list;
	}
	
	/**
	 * 获取某个日期x分钟前后的具体日期
	 * @param dateTime
	 * @param minute 负值表示x分钟前，正值表示x分钟后
	 */
	public static Date getDateByMinute(Date dateTime , int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTime);
		cal.set(Calendar.MINUTE, minute);
		return cal.getTime();
	}
	
	/**
	 * 获取包含当前天在内的一周日期
	 * @return 数组
	 */
	public static List<String> getLastestDays(int day) {
		List<String> dates = new ArrayList<String>();
		
		Calendar cal = Calendar.getInstance();
		dates.add(cnFormat(cal.getTime()));
		
		for(int i = 0; i < (day - 1) ; i++) {
			cal.add(Calendar.DAY_OF_YEAR, -1);
			dates.add(cnFormat(cal.getTime()));
		}
		
		return dates;
	}

	public static void main(String[] args) throws ParseException {
		/*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = formatter.parse("2013-07-01 01:00:00");
		Date end = formatter.parse("2013-07-01 12:00:00");
		long splitCount = 12l;
		List<Date> list = getDateSplit(start, end, splitCount);
		for (Date date : list) {
			System.out.println(formatter.format(date));
		}*/
		
//		Date start = new Date();
//		
//		
//		try {
//			Thread.sleep(1000 * 73);
//			
//			Date end = new Date();
//			int a = getMinutesSpace(end, start);
//			System.out.println(a);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		List<String> dates = getLastestDays(7);
		System.out.println(dates.size());
		for (String str : dates) {
			System.out.println(str);
		}
	}
}
