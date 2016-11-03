package org.hacker.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DateHelper for DB operate and Simple Date tools
 * 	 
 * @author 	Mr.J.
 * 
 * @since	1.0
 * **/
public class DateKit {
	/**
	 * 这个函数将返回一个星期区间，根据参数d给出的时间为中轴
	 * 比如今天是2014-10-11,那么返回的就是2014-10-06和2014-10-12
	 * 
	 * @param	d :一个日期对象
	 * @param	pattern :日期的格式
	 * 
	 * @return	String[] :<p>String[0]是StarDate</p><p>String[1]是EndDate</p>
	 * **/
	public static String[] getWeekStarDateAndEndDate(Date d, String pattern){
		 String[] results = new String[2];
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d);
		c2.setTime(d);
		int week = c1.get(Calendar.DAY_OF_WEEK)-1;
		if(week < 0)
			week = 1;
		if(week == 0){
			c1.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.DATE)-6);
			results[0] = sdf.format(c1.getTime());
			results[1] = sdf.format(d);
		}else{
			c1.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),(c1.get(Calendar.DATE)-(c1.get(Calendar.DAY_OF_WEEK)-1)+1));
			results[0] = sdf.format(c1.getTime());
			c2.set(c2.get(Calendar.YEAR),c2.get(Calendar.MONTH),c2.get(Calendar.DATE)+7-(c2.get(Calendar.DAY_OF_WEEK)-1));
			results[1] = sdf.format(c2.getTime());
		}
		return results;
	}
	
	public static String Format(Date d, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(d);
	}
	
	public static Date Format(String dateStr, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 该函数返回提前或者是推后的天数的日期的字符串
	 * 如今天是2014-10-13,number:2;b_or_l:before,则返回2014-10-11的Date对象
	 * <p>(当然日期的格式还需要自己调用格式的函数)</p>
	 * 
	 * @param	d :给定的Date对象
	 * @param	number :提前或者是推后的天数
	 * @param 	b_or_l :参数如果是"before" 则返回的是提前的天数，否则"later"返回推后的天数
	 * @throws StringParamException 
	 * **/
	public static Date getBeforeOrLaterDate(Date d,int number, String b_or_l) throws Exception{
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		if(b_or_l.equals("before")){
			c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE)-number);
		}else if(b_or_l.equals("later")){
			c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE)+number);
		}else{
			throw new Exception("b_or_l param异常,请检查你的参数是否正确--->\"before\" of \"later\"");
		}
		return c.getTime();
	}
	/**
	 * 这个函数将返回一个月区间，根据参数d给出的时间为中轴
	 * 比如今天是2014-10-11,那么返回的就是2014-10-01和2014-10-31
	 * 
	 * @param	d :一个日期对象
	 * @param	pattern :日期的格式
	 * 
	 * @return	String[] :<p>String[0]是StarDate</p><p>String[1]是EndDate</p>
	 * **/
	public static String[] getMonthStarDateAndEndDate(Date d, String pattern){
		String[] results = new String[2];
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d);
		c2.setTime(d);
		c1.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),1);
		results[0] = sdf.format(c1.getTime());
		int last = getMonthLastDay(c2.get(Calendar.YEAR),c1.get(Calendar.MONTH));
		c2.set(c2.get(Calendar.YEAR),c2.get(Calendar.MONTH),last);
		results[1] = sdf.format(c2.getTime());
		return results;
	}
	
	/**
	 * 取得当月天数
	 * */
	public static int getCurrentMonthLastDay()
	{
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);//把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 得到指定月的天数
	 * */
	public static int getMonthLastDay(int year, int month)
	{
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);//把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}
}	
