package weixinpay.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式化工具
 * @author Administrator
 * @create 2015-11-25 上午10:33:48
 */
public class DateForamtUtil
{

	private static SimpleDateFormat yyyy_MM_dd_HH_mm_ss_format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static SimpleDateFormat yyyy_MM_dd_format= new SimpleDateFormat("yyyy-MM-dd");
	
	private static SimpleDateFormat HH_mm_format= new SimpleDateFormat("HH:mm");
	
	private static SimpleDateFormat YYYYMMddHHmmss = new SimpleDateFormat("YYYYMMddHHmmss");
	
	/**
	 * 日期转换成 HH:mm 格式字符串
	 */
	public static String to_HH_mm_str(Date date){
		return HH_mm_format.format(date);
	}
	
	/**
	 * 字符串转换成 HH:mm 格式日期
	 */
	public static Date to_HH_mm_date(String dateStr) throws ParseException{
		return HH_mm_format.parse(dateStr);
	}
	
	/**
	 * 日期转换成 yyyy-MM-dd HH:mm:ss 格式字符串
	 */
	public static String to_yyyy_MM_dd_HH_mm_ss_str(Date date){
		return yyyy_MM_dd_HH_mm_ss_format.format(date);
	}
	
	/**
	 * 字符串转换成 yyyy-MM-dd HH:mm:ss 格式日期
	 */
	public static Date to_yyyy_MM_dd_HH_mm_ss_date(String dateStr) throws ParseException{
		return yyyy_MM_dd_HH_mm_ss_format.parse(dateStr);
	}
	
	/**
	 * 日期转换成 yyyy-MM-dd 格式字符串
	 */
	public static String to_yyyy_MM_dd_str(Date date){
		return yyyy_MM_dd_format.format(date);
	}
	
	/**
	 * 字符串转换成 yyyy-MM-dd 格式日期
	 */
	public static Date to_yyyy_MM_dd_date(String dateStr) throws ParseException{
		return yyyy_MM_dd_format.parse(dateStr);
	}
	
	/**
	 * 日期转换成 YYYYMMddHHmmss 格式字符串
	 */
	public static String to_YYYYMMddHHmmss_str(Date date){
		return YYYYMMddHHmmss.format(date);
	}
	
	
	/**
	 * 日期增加分钟
	 * @return
	 */
	public static Date addMinute(Date cur, int minute){
		Calendar cal = Calendar.getInstance();
		cal.setTime(cur);
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + minute);
		return cal.getTime();
	}
	
	/**
	 * 日期增加天数
	 * @return
	 */
	public static Date addDay(Date cur, int day){
		Calendar cal = Calendar.getInstance();
		cal.setTime(cur);
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + day);
		return cal.getTime();
	}
}
