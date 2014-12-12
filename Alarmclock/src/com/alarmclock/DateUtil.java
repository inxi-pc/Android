package com.alarmclock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;

public class DateUtil {

	public static boolean DEBUG = true;
	public static SimpleDateFormat fmt = new SimpleDateFormat();
	
	/**
	 * 
	 * @param dateValue	循环闹钟的触发日期字符串,如1,2,3表示周一,周二,周三触发闹钟
	 * @param startTime	闹钟触发时间,如6:00表示6点触发的闹钟.	
	 * @return
	 * 
	 * 循环闹钟触发时间nextTime[],包含各个计算好的循环时间.对于同一个闹钟,它可以设定多个循环.
	 * 如7:00闹钟可以设定为周一,周三重复,那么nextTime储存[xxxx/xx/xx 7:00 xxxx/xx/xx 7:00]
	 * 两个循环的时间,不过存储的时间是以微秒为单位的unix时间戳.
	 */
	public static long[] getNextAlarmTime(String dateValue,String startTime) {
		
	    final Calendar c = Calendar.getInstance();
	    final long now = System.currentTimeMillis();
	    // 将日历的时间设定成要触发闹钟的时间
	    try {
	        fmt.applyPattern("HH:mm");
	        Date d = fmt.parse(startTime);
	        c.set(Calendar.HOUR_OF_DAY, d.getHours());
	        c.set(Calendar.MINUTE, d.getMinutes());
	        c.set(Calendar.SECOND, 0);
	        c.set(Calendar.MILLISECOND, 0);
	    } catch (Exception e) {  
	        e.printStackTrace();
	    }
	    
        final long[] checkedWeeks = parseDateWeeks(dateValue);
        long[] nextTime = new long[checkedWeeks.length];
	    int i = 0;
        if (null != checkedWeeks) {
            for (long week : checkedWeeks) {
            	//将日历的星期数设定成触发闹钟的星期数.
            	c.set(Calendar.DAY_OF_WEEK, (int) (week+1));
                //使用日历getTimeInMillis()计算出闹钟触发的UTC时间
            	long triggerAtTime = c.getTimeInMillis();
            	// 触发时间比当前早,那么就要到下周触发.比当前时间晚当然就不用变了.
                if (triggerAtTime <= now) { 
                    triggerAtTime += AlarmManager.INTERVAL_DAY * 7;
                }
                nextTime[i] = triggerAtTime; 
                
            	if(DEBUG)
                {
            		fmt.applyPattern("dd/MM/yyyy HH:mm:ss");
            		System.out.println("repeat alarm next time is :"+fmt.format(nextTime[i]));
                }
            	i++;
            }
        }
	    return nextTime;
	}
	/**
	 * 
	 * @param startTime	闹钟触发时间
	 * @return
	 * 
	 * 	一次性闹钟触发时间nextTime = 当前UTC时间毫秒数(从1970/1/1 0:0:0开始)-当前时间毫秒数字(如9:00)+触发时间毫秒数
	 *	如过nextTime小于等于当前时间,那么触发时间自动变为第二天
	 *
	 */
	public static long getOnceNextAlarmTime(String startTime)
	{
		fmt.applyPattern("HH:mm:ss");
		long nowUTC = System.currentTimeMillis(); 
		Date now = new Date(nowUTC);
		String hhmmss = fmt.format(now);
		String[] time = hhmmss.split(":");
		long timeUTC = Long.parseLong(time[0])*3600 + 
				Long.parseLong(time[1])*60 + Long.parseLong(time[2]);
		
		String[] start = startTime.split(":");
		long nextTime = nowUTC - timeUTC*1000 + 
				(Long.parseLong(start[0])*3600 + Long.parseLong(start[1])*60)*1000;
		if(nextTime <= nowUTC)
		{
			//nextTime += AlarmManager.INTERVAL_DAY*1;
		}
		
		if(DEBUG)
		{
			Date debug = new Date(nextTime);
			fmt.applyPattern("dd/MM/yyyy HH:mm:ss");
			System.out.println("once alarm next time is :"+fmt.format(debug));
		}
		return nextTime;
	}
	
	/**
	 * 
	 * 解析选择星期数,返回星期数字(1-7)数组
	 */
	public static long[] parseDateWeeks(String value) {
	    long[] weeks = null;
	    try {
	        final String[] items = value.split(",");
	        weeks = new long[items.length];
	        int i = 0;
	        for (String s : items) {
	            weeks[i++] = Long.valueOf(s);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return weeks;
	}
}
