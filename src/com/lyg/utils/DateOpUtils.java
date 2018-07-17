package com.lyg.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
/**
 * 
 * @author Administrator
 * 时间操作工具类
 */
public class DateOpUtils {
	public static final String STYLE_COMPLETE = "yyyy-MM-dd HH:mm:ss";
	public static final String STYLE_ONLY_DATE = "yyyy-MM-dd";
	/**
	 * 自动获取时间，增减月份
	 * @param startTime  起始时间
	 * @param month      多少个月
	 * @return
	 */
	public static Date autoGetEndTime(Date startTime,int month){
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		calendar.add(GregorianCalendar.MONTH, month);
		date = calendar.getTime();
		return date ;
	}
	
	/**
	 * 根据月份操作，每月固定加30天
	 * @param startTime  起始时间
	 * @param month      多少个月
	 * @return
	 */
	public static Date autoGetEndTimeByDate(Date startTime,int month){
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		calendar.add(Calendar.DATE, month*30);
		date = calendar.getTime();
		return date ;
	}
	/**
	 * 时间操作，年为单位增减
	 * @param startTime  起始时间
	 * @param year       多少年
	 * @return           计算后的时间
	 */
	public static Date autoGetEndTimeAddYear(Date startTime,int year){
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		calendar.add(Calendar.YEAR, year);
		date = calendar.getTime();
		return date ;
	}
	
	/**
	 * 日期转换工具 将日期类型转换为字符串类型的日期
	 * 
	 * @param dt 日期类型
	 * @return 格式化成'年-月-日'类型的字符串
	 */
	public static String formatDate(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(dt);
	}

	/**
	 * 根据formatModel来格式日期类型格式
	 * 
	 * @param dt 日期类型
	 * @param formatModel 格式类型，如yyyy-MM-dd HH:mm:ss
	 * @return 字符串日期格式化类型
	 */
	public static String formatDate(Date dt, String formatModel) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatModel);
		return sdf.format(dt);
	}

	/**
	 * 日期格式化成"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param dt
	 * @return
	 */
	public static String getYyyyMMddHHmmss(Date dt) {
		return DateOpUtils.formatDate(dt, STYLE_COMPLETE);
	}

	/**
	 * 日期格式化成"yyyy-MM-dd"
	 * 
	 * @param dt
	 * @return
	 */
	public static String getYyyyMMdd(Date dt) {
		return DateOpUtils.formatDate(dt, STYLE_ONLY_DATE);
	}

	/**
	 * 将日期格式话成年月日的格式
	 * 
	 * @param dt 日期类型
	 * @return yyyyMMdd 日期类型字符串
	 */
	public static String formatDateAsyyyymmdd(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(dt);
	}

	/**
	 * 将日期格式转换成时分秒毫秒格式
	 * 
	 * @param dt
	 * @return
	 */
	public static String formatTimeAshhmiss(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSSS");
		return sdf.format(dt);
	}
	/**
	 * 将日期格式转换成年月日时分秒毫秒格式
	 * 
	 * @param dt
	 * @return
	 */
	public static String formatTimeAsyMdHmsS(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		return sdf.format(dt);
	}

	
	/**
	 * 根据输入的时间戳获取当天最小的时间戳（23:59:59）
	 * 
	 * @param timestamp 时间戳
	 * @return
	 */
	public static Long getMinTimeInMillis(Long timestamp) {
		Calendar cal = Calendar.getInstance();
		Calendar calMin = Calendar.getInstance();
		calMin.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		return calMin.getTimeInMillis();
	}

	/**
	 * 根据输入的时间戳获取当天最大的时间戳（23:59:59）
	 * 
	 * @param timestamp 时间戳
	 * @return
	 */
	public static Long getMaxTimeInMillis(Long timestamp) {
		Calendar cal = Calendar.getInstance();
		Calendar calMax = Calendar.getInstance();
		calMax.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		return calMax.getTimeInMillis();
	}
	/**
	 * 比较两个时间的大小
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return 1date1 比date2大，0相等，-1小
	 */
	public static int compareDate(Date date1,Date date2){
		int compareTo = date1.compareTo(date2);
		int result = 00;
		if(compareTo > 0){
			result = 1;
		}
		if(compareTo == 0){
			result = 0;
		}
		if(compareTo < 0){
			result = -1;
		}
		return result;
	}
	/**
	 * 比较两个时间的大小
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return 1date1 比date2大，0相等，-1小
	 */
	public static int compareDate(String date1,String date2){
		SimpleDateFormat sif = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return compareDate(sif.parse(date1),sif.parse(date2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 00;
	}
	
	
	public static void main(String[] args) {
		/*SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date();
		System.out.println("前：" + f.format(date));
		date = autoGetEndTimeAddYear(date, 12);
		System.out.println("后：" + f.format(date));*/
		
		/*String date = formatTimeAsyMdHmsS(new Date());
		System.out.println(date);*/
		int res = compareDate("2018-07-14 14:14:15","2018-07-14 14:14:12");
		System.out.println(res);
	}

}
